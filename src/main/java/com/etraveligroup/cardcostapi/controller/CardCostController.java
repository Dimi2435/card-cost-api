package com.etraveligroup.cardcostapi.controller;

import com.etraveligroup.cardcostapi.model.entity.ClearingCostEntity; // For ClearingCostEntity
import com.etraveligroup.cardcostapi.model.request.CardCostRequest;
import com.etraveligroup.cardcostapi.model.response.CardCostResponse;
import com.etraveligroup.cardcostapi.service.CardCostService;
import java.math.BigDecimal; // For BigDecimal
import java.util.List; // For List
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; // For Autowired
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Author: Dimitrios Milios
// REST controller for handling card cost requests.
// This class exposes endpoints for calculating card clearing costs.

/**
 * REST controller for handling card cost requests. This class exposes endpoints for calculating
 * card clearing costs.
 */
@RestController
@RequestMapping("/payment-cards-cost")
public class CardCostController {

  private final CardCostService cardCostService;
  private static final Logger logger = LoggerFactory.getLogger(CardCostController.class);

  @Autowired
  public CardCostController(CardCostService cardCostService) {
    this.cardCostService = cardCostService;
  }

  /**
   * Endpoint to calculate the clearing cost for a given card number.
   *
   * @param request The request containing the card number.
   * @return ResponseEntity containing the CardCostResponse.
   */
  @PostMapping
  public ResponseEntity<CardCostResponse> calculateCost(@RequestBody CardCostRequest request) {
    CardCostResponse response = cardCostService.calculateCardClearingCost(request.getCardNumber());
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
  public ResponseEntity<CardCostResponse> calculateCardClearingCost(
      @RequestBody CardCostRequest request) {
    CardCostResponse response = cardCostService.calculateCardClearingCost(request.getCardNumber());
    logger.info(
        "Calculated cost: {} for card number: {}", response.getCost(), request.getCardNumber());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  // --- Example CRUD endpoints for clearing cost matrix (requires service/repo methods) ---
  @GetMapping("/payment-cards-cost")
  public ResponseEntity<List<ClearingCostEntity>> getAllClearingCosts() {
    List<ClearingCostEntity> costs = cardCostService.getAllClearingCosts();
    return new ResponseEntity<>(costs, HttpStatus.OK);
  }

  @PutMapping("/payment-cards-cost/{countryCode}")
  public ResponseEntity<ClearingCostEntity> updateClearingCost(
      @PathVariable String countryCode, @RequestBody BigDecimal newCost) {
    ClearingCostEntity updatedCost = cardCostService.updateClearingCost(countryCode, newCost);
    return new ResponseEntity<>(updatedCost, HttpStatus.OK);
  }

  @DeleteMapping("/payment-cards-cost/{countryCode}")
  public ResponseEntity<Void> deleteClearingCost(@PathVariable String countryCode) {
    cardCostService.deleteClearingCost(countryCode);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * Endpoint to add a clearing cost.
   *
   * @param cost The clearing cost entity to be added.
   * @return ResponseEntity containing the created ClearingCostEntity.
   */
  // @PostMapping
  // public ResponseEntity<ClearingCostEntity> addClearingCost(@RequestBody ClearingCostEntity cost)
  // {
  //   ClearingCostEntity createdCost = cardCostService.addClearingCost(cost);
  //   return ResponseEntity.status(HttpStatus.CREATED).body(createdCost);
  // }
}
