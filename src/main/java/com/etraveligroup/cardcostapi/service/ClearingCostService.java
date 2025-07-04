package com.etraveligroup.cardcostapi.service;

import com.etraveligroup.cardcostapi.dto.ClearingCostResponse;
import com.etraveligroup.cardcostapi.exception.InvalidCardNumberException;
import com.etraveligroup.cardcostapi.model.ClearingCost;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal; // For BigDecimal
import java.util.List;

// Author: Dimitrios Milios
// Service interface for calculating card clearing costs.
// This interface defines the contract for card cost calculations.

/**
 * Service interface for calculating card clearing costs. This interface defines the contract for
 * card cost calculations.
 */
public interface ClearingCostService {

  /**
   * Calculates the clearing cost for a given card number.
   *
   * @param cardNumber The full card number (PAN).
   * @return ClearingCostResponse containing the country and calculated cost.
   * @throws InvalidCardNumberException if the card number is invalid or BIN lookup fails.
   */
  ClearingCostResponse calculateCardClearingCost(
      @NotBlank(message = "Card number cannot be blank") String cardNumber)
      throws InvalidCardNumberException;

  /**
   * Updates the clearing cost for a given country code.
   *
   * @param countryCode The country code to update.
   * @param newCost The new cost to set.
   * @return Updated ClearingCost entity.
   */
  ClearingCost updateClearingCost(
      @NotBlank(message = "Country code cannot be blank") String countryCode,
      @NotNull(message = "New cost cannot be null") BigDecimal newCost);

  /**
   * Deletes the clearing cost for a given country code.
   *
   * @param countryCode The country code to delete.
   */
  void deleteClearingCost(@NotBlank(message = "Country code cannot be blank") String countryCode);

  /**
   * Retrieves all clearing costs.
   *
   * @return List of ClearingCost entities.
   */
  List<ClearingCost> getAllClearingCosts();
}
