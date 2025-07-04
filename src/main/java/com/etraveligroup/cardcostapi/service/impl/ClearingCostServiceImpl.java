package com.etraveligroup.cardcostapi.service.impl;

import com.etraveligroup.cardcostapi.dto.ClearingCostResponse;
import com.etraveligroup.cardcostapi.model.ClearingCost;
import com.etraveligroup.cardcostapi.repository.ClearingCostRepository;
import com.etraveligroup.cardcostapi.service.ClearingCostService;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

// Author: Dimitrios Milios
// Implementation of ClearingCostService interface.
// This class contains the logic for calculating card clearing costs.

/**
 * Implementation of ClearingCostService interface. This class contains the logic for calculating
 * card clearing costs.
 */
@Service
public class ClearingCostServiceImpl implements ClearingCostService {

  private final ClearingCostRepository clearingCostRepository;
  private final WebClient binlistWebClient;
  private static final Logger logger = LoggerFactory.getLogger(ClearingCostServiceImpl.class);
  private static final String BINLIST_BASE_URL = "https://lookup.binlist.net/";

  @Value("${app.clearing-cost.default-country}")
  private String defaultCountryCode;

  @Value("${app.clearing-cost.default-cost}")
  private BigDecimal defaultCost;

  public ClearingCostServiceImpl(
      ClearingCostRepository clearingCostRepository, WebClient.Builder webClientBuilder) {
    this.clearingCostRepository = clearingCostRepository;
    this.binlistWebClient = webClientBuilder.baseUrl(BINLIST_BASE_URL).build();
  }

  /**
   * Calculates the clearing cost for a given card number.
   *
   * @param cardNumber The full card number (PAN).
   * @return ClearingCostResponse containing the country and calculated cost.
   * @throws IllegalArgumentException if the card number is invalid or BIN lookup fails.
   */
  @Override
  public ClearingCostResponse calculateCardClearingCost(String cardNumber) {
    logger.info("Calculating clearing cost for card number: {}", cardNumber);
    if (cardNumber == null || cardNumber.length() < 6) {
      throw new IllegalArgumentException("Card number must have at least 6 digits to extract BIN.");
    }
    String bin = cardNumber.substring(0, 6);

    String countryCode =
        binlistWebClient
            .get()
            .uri(BINLIST_BASE_URL + bin)
            .retrieve()
            .bodyToMono(BinlistResponse.class)
            .timeout(Duration.ofSeconds(5))
            .onErrorResume(
                e -> {
                  logger.error("BIN lookup failed for BIN {}: {}", bin, e.getMessage());
                  return Mono.empty();
                })
            .map(
                binlistResponse ->
                    binlistResponse.country != null ? binlistResponse.country.iso2 : null)
            .block();

    BigDecimal cost;
    String finalCountryCode;

    if (countryCode != null && !countryCode.isEmpty()) {
      finalCountryCode = countryCode.toUpperCase();
      cost =
          clearingCostRepository
              .findByCountryCode(finalCountryCode) // This returns Optional<ClearingCostEntity>
              .map(ClearingCost::getCost) // Correct: maps Optional<ClearingCostEntity> to
              // Optional<BigDecimal>
              .orElseGet(
                  () -> {
                    logger.warn(
                        "Country {} not found in clearing cost matrix, falling back to 'Others'.",
                        finalCountryCode);
                    return clearingCostRepository
                        .findByCountryCode(
                            defaultCountryCode) // Returns Optional<ClearingCostEntity>
                        .map(ClearingCost::getCost) // Correct
                        .orElse(
                            defaultCost); // Gets BigDecimal from Optional<BigDecimal> or default
                  });
    } else {
      finalCountryCode = defaultCountryCode;
      cost =
          clearingCostRepository
              .findByCountryCode(defaultCountryCode) // Returns Optional<ClearingCostEntity>
              .map(ClearingCost::getCost) // Correct
              .orElse(defaultCost); // Gets BigDecimal from Optional<BigDecimal> or default
    }

    logger.info("Calculated cost: {} for country: {}", cost, finalCountryCode);
    return new ClearingCostResponse(finalCountryCode, cost);
  }

  @Override
  public ClearingCost updateClearingCost(String countryCode, BigDecimal newCost) {
    // This line is correct if findByCountryCode returns Optional<ClearingCostEntity>
    ClearingCost costEntity =
        clearingCostRepository
            .findByCountryCode(countryCode)
            .orElseThrow(() -> new IllegalArgumentException("Country not found: " + countryCode));
    costEntity.setCost(newCost);
    return costEntity; // You might want to save it here: clearingCostRepository.save(costEntity);
  }

  @Override
  public void deleteClearingCost(String countryCode) {
    // This line is correct if findByCountryCode returns Optional<ClearingCostEntity>
    ClearingCost costEntity =
        clearingCostRepository
            .findByCountryCode(countryCode)
            .orElseThrow(() -> new IllegalArgumentException("Country not found: " + countryCode));
    clearingCostRepository.delete(costEntity);
  }

  @Override
  public List<ClearingCost> getAllClearingCosts() {
    return clearingCostRepository.findAll();
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  private static class BinlistResponse {
    private Country country;

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
      private String iso2;
    }
  }
}
