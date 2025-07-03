package com.etraveligroup.cardcostapi.service;

import com.etraveligroup.cardcostapi.model.entity.ClearingCostEntity;
import com.etraveligroup.cardcostapi.model.response.CardCostResponse;
import java.math.BigDecimal; // For BigDecimal
import java.util.List;

// Author: Dimitrios Milios
// Service interface for calculating card clearing costs.
// This interface defines the contract for card cost calculations.

/**
 * Service interface for calculating card clearing costs. This interface defines the contract for
 * card cost calculations.
 */
public interface CardCostService {

  /**
   * Calculates the clearing cost for a given card number.
   *
   * @param cardNumber The full card number (PAN).
   * @return CardCostResponse containing the country and calculated cost.
   * @throws IllegalArgumentException if the card number is invalid or BIN lookup fails.
   */
  CardCostResponse calculateCardClearingCost(String cardNumber);

  // Methods for CRUD on clearing cost matrix could also be added here, e.g.:
  ClearingCostEntity updateClearingCost(String countryCode, BigDecimal newCost);

  void deleteClearingCost(String countryCode);

  List<ClearingCostEntity> getAllClearingCosts();
}
