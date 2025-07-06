package com.etraveligroup.cardcostapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for card cost request payload. This class contains validation annotations to ensure the card
 * number is valid.
 *
 * <p>Author: Dimitrios Milios
 */
@Data
public class CalculateClearingCostRequest {

  @NotBlank(message = "Card number cannot be empty")
  @Size(min = 8, max = 19, message = "Card number must be between 8 and 19 digits")
  @Pattern(regexp = "^[0-9]+$", message = "Card number must contain only digits")
  @Schema(description = "The card number (PAN) for which the cost is to be calculated.")
  private String cardNumber;

  // You might add a custom validation here for Luhn algorithm if desired.
}
