package com.etraveligroup.cardcostapi.service.impl;

import com.etraveligroup.cardcostapi.model.entity.ClearingCostEntity;
import com.etraveligroup.cardcostapi.model.response.CardCostResponse;
import com.etraveligroup.cardcostapi.repository.ClearingCostRepository;
import com.etraveligroup.cardcostapi.service.CardCostService;
import java.math.BigDecimal;
import java.time.Duration;
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
// Implementation of CardCostService interface.
// This class contains the logic for calculating card clearing costs.

/**
 * Implementation of CardCostService interface.
 * This class contains the logic for calculating card clearing costs.
 */
@Service
public class CardCostServiceImpl implements CardCostService {

  private final ClearingCostRepository clearingCostRepository;
  private final WebClient binlistWebClient;
  private static final Logger logger = LoggerFactory.getLogger(CardCostServiceImpl.class);

  @Value("${app.clearing-cost.default-country:OTHERS}")
  private String defaultCountryCode;

  @Value("${app.clearing-cost.default-cost:10.00}")
  private BigDecimal defaultCost;

  public CardCostServiceImpl(
      ClearingCostRepository clearingCostRepository, WebClient.Builder webClientBuilder) {
    this.clearingCostRepository = clearingCostRepository;
    this.binlistWebClient = webClientBuilder.baseUrl("https://binlist.net/").build();
  }

  /**
   * Calculates the clearing cost for a given card number.
   *
   * @param cardNumber The full card number (PAN).
   * @return CardCostResponse containing the country and calculated cost.
   * @throws IllegalArgumentException if the card number is invalid or BIN lookup fails.
   */
  @Override
  public CardCostResponse calculateCardClearingCost(String cardNumber) {
    logger.info("Calculating clearing cost for card number: {}", cardNumber);
    if (cardNumber == null || cardNumber.length() < 6) {
      throw new IllegalArgumentException("Card number must have at least 6 digits to extract BIN.");
    }
    String bin = cardNumber.substring(0, 6);

    String countryCode =
        binlistWebClient
            .get()
            .uri("/" + bin)
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
              .findByCountryCode(finalCountryCode)
              .map(ClearingCostEntity::getCost)
              .orElseGet(
                  () -> {
                    logger.warn("Country {} not found in clearing cost matrix, falling back to 'Others'.", finalCountryCode);
                    return clearingCostRepository
                        .findByCountryCode(defaultCountryCode)
                        .map(ClearingCostEntity::getCost)
                        .orElse(defaultCost);
                  });
    } else {
      finalCountryCode = defaultCountryCode;
      cost =
          clearingCostRepository
              .findByCountryCode(defaultCountryCode)
              .map(ClearingCostEntity::getCost)
              .orElse(defaultCost);
    }

    logger.info("Calculated cost: {} for country: {}", cost, finalCountryCode);
    return new CardCostResponse(finalCountryCode, cost);
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
