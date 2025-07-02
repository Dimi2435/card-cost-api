package com.etraveligroup.cardcostapi.service.impl;

import com.etraveligroup.cardcostapi.model.entity.ClearingCostEntity;

import com.etraveligroup.cardcostapi.model.response.CardCostResponse;
import com.etraveligroup.cardcostapi.repository.ClearingCostRepository;
import com.etraveligroup.cardcostapi.service.CardCostService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Author: Dimitrios Milios
// Implementation of CardCostService interface.
// This class contains the logic for calculating card clearing costs.
@Service
public class CardCostServiceImpl implements CardCostService {

    private final ClearingCostRepository clearingCostRepository;
    private final WebClient binlistWebClient;

    //[cite_start]// Configuration for the "Others" country code and default cost [cite: 2, 19]
    @Value("${app.clearing-cost.default-country:OTHERS}")
    private String defaultCountryCode;
    @Value("${app.clearing-cost.default-cost:10.00}")
    private BigDecimal defaultCost;


    public CardCostServiceImpl(ClearingCostRepository clearingCostRepository, WebClient.Builder webClientBuilder) {
        this.clearingCostRepository = clearingCostRepository;
        //[cite_start]
        this.binlistWebClient = webClientBuilder.baseUrl("https://binlist.net/").build(); 
        //[cite: 2, 23]
    }

    @Override
    public CardCostResponse calculateCardClearingCost(String cardNumber) {
        //[cite_start]// 1. Extract BIN (first 6 digits) [cite: 14]
        if (cardNumber == null || cardNumber.length() < 6) {
            throw new IllegalArgumentException("Card number must have at least 6 digits to extract BIN.");
        }
        String bin = cardNumber.substring(0, 6);

        // 2. Call BIN Lookup API (https://binlist.net/)
        //[cite_start]// Make sure to handle SLA and potential failures [cite: 2, 33]
        // This is a simplified example, proper error handling and retry logic is crucial here
        String countryCode = binlistWebClient.get()
                .uri("/" + bin)
                .retrieve()
                .bodyToMono(BinlistResponse.class) // Map to a simple DTO for binlist response
                .timeout(Duration.ofSeconds(5)) // Example timeout
                .onErrorResume(e -> {
                    // Log error and potentially return a fallback or throw a custom exception
                    System.err.println("BIN lookup failed for BIN " + bin + ": " + e.getMessage());
                    // For now, let's return a Mono.empty() to signify no country found via BINList
                    return Mono.empty();
                })
                .map(binlistResponse -> binlistResponse.country != null ? binlistResponse.country.iso2 : null)
                .block(); // Block for simplicity in a sync flow; consider reactive for high scale


        // 3. Look up clearing cost based on country code
        BigDecimal cost;
        String finalCountryCode;

        if (countryCode != null && !countryCode.isEmpty()) {
            finalCountryCode = countryCode.toUpperCase();
            cost = clearingCostRepository.findByCountryCode(finalCountryCode)
                                       .map(ClearingCostEntity::getCost)
                                       .orElseGet(() -> {
                                           //[cite_start]// Fallback to "Others" if specific country not found in matrix [cite: 2, 19]
                                           System.out.println("Country " + finalCountryCode + " not found in clearing cost matrix, falling back to 'Others'.");
                                           return clearingCostRepository.findByCountryCode(defaultCountryCode)
                                                                      .map(ClearingCostEntity::getCost)
                                                                      .orElse(defaultCost); // Default to a hardcoded value if 'Others' isn't even in DB
                                       });
        } else {
            //[cite_start]// If BIN lookup failed or returned no country, use "Others" [cite: 2, 19]
            finalCountryCode = defaultCountryCode;
            cost = clearingCostRepository.findByCountryCode(defaultCountryCode)
                                       .map(ClearingCostEntity::getCost)
                                       .orElse(defaultCost);
        }

        return new CardCostResponse(finalCountryCode, cost);
    }

    // --- Inner class for Binlist API response mapping ---
    // You would typically define this in a separate model package (e.g., model.thirdparty.binlist)
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
            private String iso2; //[cite_start]// The ISO2 code you need [cite: 3, 47]
        }
    }
}