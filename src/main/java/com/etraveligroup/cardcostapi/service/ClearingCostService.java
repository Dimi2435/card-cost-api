package com.etraveligroup.cardcostapi.service;

import com.etraveligroup.cardcostapi.dto.ClearingCostResponse;
import com.etraveligroup.cardcostapi.dto.CreateClearingCostRequest;
import com.etraveligroup.cardcostapi.dto.PagedResponse;
import com.etraveligroup.cardcostapi.dto.UpdateClearingCostRequest;
import com.etraveligroup.cardcostapi.exception.InvalidCardNumberException;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.data.domain.Pageable;

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
   * Creates a new clearing cost entry.
   *
   * @param request The request containing the country code and cost.
   * @return ClearingCostResponse containing the created clearing cost details.
   */
  ClearingCostResponse createClearingCost(CreateClearingCostRequest request);

  /**
   * Retrieves all clearing costs.
   *
   * @return List of ClearingCostResponse entities.
   */
  List<ClearingCostResponse> getAllClearingCosts();

  /**
   * Retrieves all clearing costs with pagination support.
   *
   * @param pageable Pagination parameters (page, size, sort)
   * @return PagedResponse containing ClearingCostResponse entities
   */
  PagedResponse<ClearingCostResponse> getAllClearingCosts(Pageable pageable);

  /**
   * Retrieves a clearing cost by ID.
   *
   * @param id The ID of the clearing cost.
   * @return ClearingCostResponse containing the clearing cost details.
   */
  ClearingCostResponse getClearingCostById(Long id);

  /**
   * Updates the clearing cost for a given ID.
   *
   * @param id The ID of the clearing cost to update.
   * @param request The request containing the new clearing cost details.
   * @return Updated ClearingCostResponse.
   */
  ClearingCostResponse updateClearingCost(Long id, UpdateClearingCostRequest request);

  /**
   * Deletes the clearing cost for a given ID.
   *
   * @param id The ID of the clearing cost to delete.
   */
  void deleteClearingCost(Long id);
}
