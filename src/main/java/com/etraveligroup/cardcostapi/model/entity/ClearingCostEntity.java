package com.etraveligroup.cardcostapi.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

// Author: Dimitrios Milios
// JPA entity representing the clearing cost for different countries.
// This class maps to the 'clearing_cost' table in the database.
// It contains fields for country code and associated cost.

/**
 * JPA entity representing the clearing cost for different countries.
 * This class maps to the 'clearing_cost' table in the database.
 * It contains fields for country code and associated cost.
 */
@Entity
@Table(name = "clearing_cost")
@Data
@NoArgsConstructor
public class ClearingCostEntity {

  @Id
  private String countryCode; // e.g., "US", "GR", "Others"
  private BigDecimal cost; // e.g., $5, $15, $10

  public ClearingCostEntity(String countryCode, BigDecimal cost) {
    this.countryCode = countryCode;
    this.cost = cost;
  }

  public BigDecimal getCost() {
    return this.cost;
  }
}
