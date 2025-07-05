package com.etraveligroup.cardcostapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO for creating a new clearing cost entry. Contains validation for country code and cost. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateClearingCostRequest {

  @Schema(description = "The ISO2 country code (2 characters)")
  @NotBlank(message = "Country code cannot be blank")
  @Size(min = 2, max = 2, message = "Country code must be exactly 2 characters")
  private String countryCode;

  @Schema(description = "The clearing cost for the country")
  @NotNull(message = "Cost cannot be null")
  @DecimalMin(value = "0.0", inclusive = false, message = "Cost must be greater than zero")
  private BigDecimal cost;
}
