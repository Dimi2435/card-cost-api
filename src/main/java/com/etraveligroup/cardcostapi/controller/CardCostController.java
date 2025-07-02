package com.etraveligroup.cardcostapi.controller;

import com.etraveligroup.cardcostapi.model.request.CardCostRequest;
import com.etraveligroup.cardcostapi.model.response.CardCostResponse;
import com.etraveligroup.cardcostapi.service.CardCostService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Author: Dimitrios Milios
// REST controller for handling card cost requests.
// This class exposes endpoints for calculating card clearing costs.

/**
 * REST controller for handling card cost requests.
 * This class exposes endpoints for calculating card clearing costs.
 */
@RestController
@RequestMapping("/payment-cards-cost")
public class CardCostController {

  private final CardCostService cardCostService;
  private static final Logger logger = LoggerFactory.getLogger(CardCostController.class);

  public CardCostController(CardCostService cardCostService) {
    this.cardCostService = cardCostService;
  }

  /**
   * Calculates the clearing cost for a given card number.
   *
   * @param request The request containing the card number.
   * @return ResponseEntity containing the calculated cost and HTTP status.
   */
  @PostMapping
  public ResponseEntity<CardCostResponse> calculateCost(
      @Valid @RequestBody CardCostRequest request) {
    logger.info("Calculating cost for card number: {}", request.getCardNumber());
    CardCostResponse response = cardCostService.calculateCardClearingCost(request.getCardNumber());
    logger.info("Calculated cost: {} for card number: {}", response.getCost(), request.getCardNumber());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  // --- Example CRUD endpoints for clearing cost matrix (requires service/repo methods) ---
  // @GetMapping("/clearing-costs")
  // public ResponseEntity<List<ClearingCostEntity>> getAllClearingCosts() { /* ... */ }

  // @PostMapping("/clearing-costs")
  // public ResponseEntity<ClearingCostEntity> addClearingCost(@RequestBody ClearingCostEntity cost)
  // { /* ... */ }

  // @PutMapping("/clearing-costs/{countryCode}")
  // public ResponseEntity<ClearingCostEntity> updateClearingCost(@PathVariable String countryCode,
  // @RequestBody BigDecimal newCost) { /* ... */ }

  // @DeleteMapping("/clearing-costs/{countryCode}")
  // public ResponseEntity<Void> deleteClearingCost(@PathVariable String countryCode) { /* ... */ }
}
