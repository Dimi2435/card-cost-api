package com.etraveligroup.cardcostapi.model.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor; // Requires Lombok
import lombok.Data; // Requires Lombok
import lombok.NoArgsConstructor; // Requires Lombok

// Author: Dimitrios Milios
// DTO for card cost response payload.
// This class contains the response structure for card cost calculations.

/**
 * DTO for card cost response payload.
 * This class contains the response structure for card cost calculations.
 */
@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class CardCostResponse {
  private String country; // ISO2 code
  private BigDecimal cost; // Decimal cost
}
