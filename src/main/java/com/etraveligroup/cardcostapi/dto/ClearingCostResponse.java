// This file has been renamed to ClearingCostResponse.java
package com.etraveligroup.cardcostapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor; // Requires Lombok
import lombok.Data; // Requires Lombok
import lombok.NoArgsConstructor; // Requires Lombok

/**
 * DTO for card cost response payload. This class contains the response structure for card cost
 * calculations.
 *
 * <p>Author: Dimitrios Milios
 */
@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class ClearingCostResponse {
  @Schema(description = "The ISO2 country code associated with the card.")
  private String country; // ISO2 code

  @Schema(description = "The calculated cost associated with the card.")
  private BigDecimal cost; // Decimal cost
}
