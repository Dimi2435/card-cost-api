package com.etraveligroup.cardcostapi.service.impl;

import com.etraveligroup.cardcostapi.dto.ClearingCostResponse;
import com.etraveligroup.cardcostapi.dto.CreateClearingCostRequest;
import com.etraveligroup.cardcostapi.dto.PagedResponse;
import com.etraveligroup.cardcostapi.dto.UpdateClearingCostRequest;
import com.etraveligroup.cardcostapi.exception.CostNotFoundException;
import com.etraveligroup.cardcostapi.exception.InvalidCardNumberException;
import com.etraveligroup.cardcostapi.model.ClearingCost;
import com.etraveligroup.cardcostapi.repository.ClearingCostRepository;
import com.etraveligroup.cardcostapi.service.ClearingCostService;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
// import org.springframework.cache.annotation.CacheEvict;  // Temporarily disabled
// import org.springframework.cache.annotation.Cacheable; // Temporarily disabled
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Implementation of ClearingCostService interface. This class contains the logic for calculating
 * card clearing costs.
 *
 * <p>Author: Dimitrios Milios
 */
@Service
public class ClearingCostServiceImpl implements ClearingCostService {

  private final ClearingCostRepository clearingCostRepository;
  private final WebClient binlistWebClient;
  private static final Logger logger = LoggerFactory.getLogger(ClearingCostServiceImpl.class);

  @Value("${app.external.binlist.timeout}")
  private int binlistTimeout;

  @Value("${app.external.binlist.retry-attempts}")
  private int retryAttempts;

  @Value("${app.clearing-cost.default-country}")
  private String defaultCountryCode;

  @Value("${app.clearing-cost.default-cost}")
  private BigDecimal defaultCost;

  public ClearingCostServiceImpl(
      ClearingCostRepository clearingCostRepository, WebClient binlistWebClient) {
    this.clearingCostRepository = clearingCostRepository;
    this.binlistWebClient = binlistWebClient;
  }

  /**
   * Calculates the clearing cost for a given card number.
   *
   * @param cardNumber The full card number (PAN).
   * @return ClearingCostResponse containing the country and calculated cost.
   * @throws InvalidCardNumberException if the card number is invalid.
   */
  @Override
  public ClearingCostResponse calculateCardClearingCost(
      @NotBlank(message = "Card number cannot be blank") String cardNumber) {

    // Validate card number is not null or empty first
    if (cardNumber == null || cardNumber.trim().isEmpty()) {
      throw new InvalidCardNumberException("Card number cannot be blank");
    }

    logger.info(
        "Calculating clearing cost for card number: {}",
        cardNumber.substring(0, Math.min(6, cardNumber.length())) + "******");

    // Remove any non-digit characters
    String cleanCardNumber = cardNumber.replaceAll("[^0-9]", "");

    if (cleanCardNumber.length() < 6) {
      throw new InvalidCardNumberException(
          "Card number must have at least 6 digits to extract BIN.");
    }

    String bin = cleanCardNumber.substring(0, 6);
    String countryCode = null;

    try {
      // Try to fetch country code from external API
      BinlistResponse binlistResponse =
          binlistWebClient
              .get()
              .uri("/" + bin)
              .retrieve()
              .bodyToMono(BinlistResponse.class)
              .timeout(Duration.ofMillis(binlistTimeout))
              .retry(retryAttempts)
              .onErrorResume(
                  e -> {
                    logger.warn("BIN lookup failed for BIN {}: {}", bin, e.getMessage());
                    return Mono.empty();
                  })
              .block();

      if (binlistResponse != null
          && binlistResponse.country != null
          && binlistResponse.country.alpha2 != null
          && !binlistResponse.country.alpha2.trim().isEmpty()) {
        countryCode = binlistResponse.country.alpha2.toUpperCase();
        logger.debug("Successfully retrieved country code: {} for BIN: {}", countryCode, bin);
      } else {
        logger.warn("No country information found for BIN: {}, using default", bin);
      }
    } catch (Exception e) {
      logger.error("Error during BIN lookup for BIN {}: {}", bin, e.getMessage());
      // Continue with default country code
    }

    BigDecimal cost;
    String finalCountryCode;

    if (countryCode != null && !countryCode.isEmpty()) {
      finalCountryCode = countryCode;
      cost =
          clearingCostRepository
              .findByCountryCode(finalCountryCode)
              .map(ClearingCost::getCost)
              .orElseGet(
                  () -> {
                    logger.warn(
                        "Country {} not found in clearing cost matrix, falling back to default '{}'.",
                        finalCountryCode,
                        defaultCountryCode);
                    return clearingCostRepository
                        .findByCountryCode(defaultCountryCode)
                        .map(ClearingCost::getCost)
                        .orElse(defaultCost);
                  });
    } else {
      logger.info("Using default country code: {}", defaultCountryCode);
      finalCountryCode = defaultCountryCode;
      cost =
          clearingCostRepository
              .findByCountryCode(defaultCountryCode)
              .map(ClearingCost::getCost)
              .orElse(defaultCost);
    }

    // Ensure cost is never null
    if (cost == null) {
      logger.warn("Cost is null, using default cost: {}", defaultCost);
      cost = defaultCost;
    }

    logger.info("Calculated cost: {} for country: {}", cost, finalCountryCode);
    return new ClearingCostResponse(finalCountryCode, cost);
  }

  /**
   * Creates a new clearing cost entry.
   *
   * @param request The request containing the country code and cost.
   * @return ClearingCostResponse containing the created clearing cost details.
   */
  @Override
  @Transactional
  public ClearingCostResponse createClearingCost(CreateClearingCostRequest request) {
    // Check if country code already exists
    Optional<ClearingCost> existingCost =
        clearingCostRepository.findByCountryCode(request.getCountryCode().toUpperCase());
    if (existingCost.isPresent()) {
      throw new IllegalArgumentException(
          "Clearing cost already exists for country: " + request.getCountryCode());
    }

    ClearingCost newCost = new ClearingCost();
    newCost.setCountryCode(request.getCountryCode().toUpperCase());
    newCost.setCost(request.getCost());

    ClearingCost savedCost = clearingCostRepository.save(newCost);
    return new ClearingCostResponse(savedCost.getCountryCode(), savedCost.getCost());
  }

  /**
   * Retrieves all clearing costs.
   *
   * @return List of ClearingCostResponse entities.
   */
  @Override
  // @Cacheable(value = "clearingCosts", key = "#countryCode")  // Temporarily disabled
  public List<ClearingCostResponse> getAllClearingCosts() {
    return clearingCostRepository.findAll().stream()
        .map(cost -> new ClearingCostResponse(cost.getCountryCode(), cost.getCost()))
        .collect(Collectors.toList());
  }

  /**
   * Retrieves all clearing costs with pagination support.
   *
   * @param pageable Pagination parameters (page, size, sort)
   * @return PagedResponse containing ClearingCostResponse entities
   */
  @Override
  // @Cacheable(value = "clearingCostsPaged", key = "#pageable.pageNumber + '_' +
  // #pageable.pageSize")  // Temporarily disabled
  public PagedResponse<ClearingCostResponse> getAllClearingCosts(Pageable pageable) {
    Page<ClearingCost> page = clearingCostRepository.findAll(pageable);

    List<ClearingCostResponse> content =
        page.getContent().stream()
            .map(cost -> new ClearingCostResponse(cost.getCountryCode(), cost.getCost()))
            .collect(Collectors.toList());

    return new PagedResponse<>(
        content,
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages(),
        page.isFirst(),
        page.isLast(),
        page.getNumberOfElements(),
        page.isEmpty());
  }

  /**
   * Retrieves a clearing cost by ID.
   *
   * @param id The ID of the clearing cost.
   * @return ClearingCostResponse containing the clearing cost details.
   */
  @Override
  // @Cacheable(value = "clearingCosts", key = "#id")  // Temporarily disabled
  public ClearingCostResponse getClearingCostById(Long id) {
    ClearingCost cost =
        clearingCostRepository
            .findById(id)
            .orElseThrow(() -> new CostNotFoundException("Clearing cost not found for ID: " + id));
    return new ClearingCostResponse(cost.getCountryCode(), cost.getCost());
  }

  /**
   * Updates the clearing cost for a given ID.
   *
   * @param id The ID of the clearing cost to update.
   * @param request The request containing the new clearing cost details.
   * @return Updated ClearingCostResponse.
   */
  @Override
  // @CacheEvict(value = "clearingCosts", key = "#id")  // Temporarily disabled
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public ClearingCostResponse updateClearingCost(Long id, UpdateClearingCostRequest request) {
    ClearingCost cost =
        clearingCostRepository
            .findById(id)
            .orElseThrow(() -> new CostNotFoundException("Clearing cost not found for ID: " + id));
    cost.setCost(request.getCost());
    clearingCostRepository.save(cost);
    return new ClearingCostResponse(cost.getCountryCode(), cost.getCost());
  }

  /**
   * Deletes the clearing cost for a given ID.
   *
   * @param id The ID of the clearing cost to delete.
   */
  @Override
  // @CacheEvict(value = "clearingCosts", key = "#id")  // Temporarily disabled
  @Transactional
  public void deleteClearingCost(Long id) {
    ClearingCost cost =
        clearingCostRepository
            .findById(id)
            .orElseThrow(() -> new CostNotFoundException("Clearing cost not found for ID: " + id));
    clearingCostRepository.delete(cost);
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  private static class BinlistResponse {
    private Number number;
    private String scheme;
    private String type;
    private String brand;
    private boolean prepaid;
    private Country country;
    private Bank bank;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Number {
      private int length;
      private boolean luhn;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Country {
      private String name;
      private String emoji;
      private String currency;
      private String latitude;
      private String longitude;
      private String numeric;
      private String alpha2;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Bank {
      private String name;
      private String url;
      private String phone;
      private String city;
    }
  }
}
