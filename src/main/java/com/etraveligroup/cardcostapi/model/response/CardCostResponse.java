package com.etraveligroup.cardcostapi.model.response;

import lombok.AllArgsConstructor; // Requires Lombok
import lombok.Data; // Requires Lombok
import lombok.NoArgsConstructor; // Requires Lombok

import java.math.BigDecimal;

@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class CardCostResponse {
    private String country; //[cite_start]// ISO2 code [cite: 3, 47]
    private BigDecimal cost; //[cite_start]// Decimal cost [cite: 3, 49]
}