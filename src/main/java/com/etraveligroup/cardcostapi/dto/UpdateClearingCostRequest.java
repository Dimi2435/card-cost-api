package com.etraveligroup.cardcostapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing clearing cost entry. Contains validation for the cost.
 *
 * <p>Author: Dimitrios Milios
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClearingCostRequest {

  @NotNull(message = "Cost cannot be null")
  @DecimalMin(value = "0.0", inclusive = false, message = "Cost must be greater than zero")
  private BigDecimal cost; // Field for the cost

  // Getter and Setter
  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }
}
