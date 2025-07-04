package com.etraveligroup.cardcostapi.controller;

import com.etraveligroup.cardcostapi.dto.ClearingCostRequest;
import com.etraveligroup.cardcostapi.dto.ClearingCostResponse;
import com.etraveligroup.cardcostapi.model.ClearingCost;
import com.etraveligroup.cardcostapi.service.ClearingCostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Author: Dimitrios Milios
// REST controller for handling clearing cost requests.
/**
 * REST controller for handling clearing cost requests. This class exposes endpoints for calculating
 * clearing costs.
 */
@RestController
@RequestMapping("/payment-cards-cost")
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
   * @return ResponseEntity containing the CardCostResponse.
   */
  @Operation(summary = "Calculate the clearing cost for a given card number")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Cost calculated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid card number")
      })
  @PostMapping
  public ResponseEntity<ClearingCostResponse> calculateCost(
      @RequestBody ClearingCostRequest request) {
    ClearingCostResponse response =
        clearingCostService.calculateCardClearingCost(request.getCardNumber());
    logger.info(
        "Calculated cost: {} for card number: {}", response.getCost(), request.getCardNumber());
    return ResponseEntity.ok(response);
  }

  /**
   * Endpoint to get the cost for a given card number.
   *
   * @param request The request containing the card number.
   * @return ResponseEntity containing the country and cost.
   */
  @PostMapping("/payment-cards-cost")
  public ResponseEntity<ClearingCostResponse> calculateCardClearingCost(
      @RequestBody ClearingCostRequest request) {
    ClearingCostResponse response =
        clearingCostService.calculateCardClearingCost(request.getCardNumber());
    logger.info(
        "Calculated cost: {} for card number: {}", response.getCost(), request.getCardNumber());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @Operation(summary = "Get all clearing costs")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved clearing costs"),
        @ApiResponse(responseCode = "404", description = "No clearing costs found")
      })
  @GetMapping("/payment-cards-cost")
  public ResponseEntity<List<ClearingCost>> getAllClearingCosts() {
    List<ClearingCost> costs = clearingCostService.getAllClearingCosts();
    return new ResponseEntity<>(costs, HttpStatus.OK);
  }

  @Operation(summary = "Update clearing cost for a specific country")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Clearing cost updated successfully"),
        @ApiResponse(responseCode = "404", description = "Country not found")
      })
  @PutMapping("/payment-cards-cost/{countryCode}")
  public ResponseEntity<ClearingCost> updateClearingCost(
      @PathVariable String countryCode, @RequestBody BigDecimal newCost) {
    ClearingCost updatedCost = clearingCostService.updateClearingCost(countryCode, newCost);
    return new ResponseEntity<>(updatedCost, HttpStatus.OK);
  }

  @Operation(summary = "Delete clearing cost for a specific country")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Clearing cost deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Country not found")
      })
  @DeleteMapping("/payment-cards-cost/{countryCode}")
  public ResponseEntity<Void> deleteClearingCost(@PathVariable String countryCode) {
    clearingCostService.deleteClearingCost(countryCode);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
