package com.etraveligroup.cardcostapi.controller;

import com.etraveligroup.cardcostapi.model.request.CardCostRequest;
import com.etraveligroup.cardcostapi.model.response.CardCostResponse;
import com.etraveligroup.cardcostapi.service.CardCostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Author: Dimitrios Milios
// REST controller for handling card cost requests.
// This class exposes endpoints for calculating card clearing costs.

@RestController
// [cite_start]
@RequestMapping("/payment-cards-cost") // [cite: 3, 42]
public class CardCostController {

  private final CardCostService cardCostService;

  public CardCostController(CardCostService cardCostService) {
    this.cardCostService = cardCostService;
  }

  @PostMapping
  public ResponseEntity<CardCostResponse> calculateCost(
      @Valid @RequestBody CardCostRequest request) {
    // Basic input validation already handled by @Valid and DTO annotations
    // Additional business logic validation can go in the service layer

    CardCostResponse response = cardCostService.calculateCardClearingCost(request.getCardNumber());
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
