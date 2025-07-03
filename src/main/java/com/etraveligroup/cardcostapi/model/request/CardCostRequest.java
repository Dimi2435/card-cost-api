package com.etraveligroup.cardcostapi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

// Author: Dimitrios Milios
// DTO for card cost request payload.
// This class contains validation annotations to ensure the card number is valid.

/**
 * DTO for card cost request payload. This class contains validation annotations to ensure the card
 * number is valid.
 */
@Data
public class CardCostRequest {

  @NotBlank(message = "Card number cannot be empty")
  @Size(min = 8, max = 19, message = "Card number must be between 8 and 19 digits")
  @Pattern(regexp = "^[0-9]+$", message = "Card number must contain only digits")
  private String cardNumber;

  // You might add a custom validation here for Luhn algorithm if desired.
}
