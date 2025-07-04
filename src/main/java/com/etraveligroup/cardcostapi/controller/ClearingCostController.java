package com.etraveligroup.cardcostapi.controller;

import com.etraveligroup.cardcostapi.dto.CalculateClearingCostRequest;
import com.etraveligroup.cardcostapi.dto.ClearingCostResponse;
import com.etraveligroup.cardcostapi.dto.UpdateClearingCostRequest;
import com.etraveligroup.cardcostapi.service.ClearingCostService;
import com.etraveligroup.cardcostapi.util.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// Author: Dimitrios Milios
// REST controller for handling clearing cost requests.
/**
 * REST controller for handling clearing cost requests. This class exposes endpoints for calculating
 * clearing costs.
 */
@RestController
@RequestMapping(
    AppConstants.API_BASE_PATH
        + AppConstants.VERSIONED_API_PATH
        + AppConstants.PAYMENT_CARDS_COST_ENDPOINT)
public class ClearingCostController {

  private final ClearingCostService clearingCostService;

  private static final Logger logger = LoggerFactory.getLogger(ClearingCostController.class);

  @Autowired
  public ClearingCostController(ClearingCostService clearingCostService) {
    this.clearingCostService = clearingCostService;
  }

  /**
   * Endpoint to calculate the clearing cost for a given card number.
   *
   * @param request The request containing the card number.
   * @param version The version of the API.
   * @return ResponseEntity containing the CardCostResponse.
   */
  @Operation(summary = "Calculate the clearing cost for a given card number")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Cost calculated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid card number")
      })
  @PostMapping
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<ClearingCostResponse> calculateCost(
      @RequestBody CalculateClearingCostRequest request,
      @RequestParam(value = "version", defaultValue = "/v"+AppConstants.)
          String version) {
    logger.info("API Version: {}", version);
    ClearingCostResponse response =
        clearingCostService.calculateCardClearingCost(request.getCardNumber());
    logger.info(
        "Calculated cost: {} for card number: {}", response.getCost(), request.getCardNumber());
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Get all clearing costs")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved clearing costs"),
        @ApiResponse(responseCode = "404", description = "No clearing costs found")
      })
  @GetMapping("/all")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<ClearingCostResponse>> getAllClearingCosts() {
    List<ClearingCostResponse> costs = clearingCostService.getAllClearingCosts();
    return ResponseEntity.ok(costs);
  }

  @Operation(summary = "Get a clearing cost by ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved clearing cost"),
        @ApiResponse(responseCode = "404", description = "Clearing cost not found")
      })
  @GetMapping("/{id}")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<ClearingCostResponse> getClearingCostById(@PathVariable Long id) {
    ClearingCostResponse cost = clearingCostService.getClearingCostById(id);
    return ResponseEntity.ok(cost);
  }

  @Operation(summary = "Update a clearing cost")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Clearing cost updated successfully"),
        @ApiResponse(responseCode = "404", description = "Clearing cost not found")
      })
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ClearingCostResponse> updateClearingCost(
      @PathVariable Long id, @RequestBody UpdateClearingCostRequest request) {
    ClearingCostResponse updatedCost = clearingCostService.updateClearingCost(id, request);
    return ResponseEntity.ok(updatedCost);
  }

  @Operation(summary = "Delete a clearing cost")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Clearing cost deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Clearing cost not found")
      })
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteClearingCost(@PathVariable Long id) {
    clearingCostService.deleteClearingCost(id);
    return ResponseEntity.noContent().build();
  }
}
