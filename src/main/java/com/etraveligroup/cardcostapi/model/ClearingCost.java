package com.etraveligroup.cardcostapi.model;

import jakarta.persistence.*; // For JPA annotations like @Entity, @Table, etc.
import jakarta.validation.constraints.DecimalMin; // For validating minimum value
import jakarta.validation.constraints.NotNull; // For validating non-null values
import jakarta.validation.constraints.Size; // For validating size constraints
import java.math.BigDecimal; // For BigDecimal
import lombok.Data; // For Lombok's @Data annotation

// Author: Dimitrios Milios
// Entity representing the clearing cost data.

/** Entity representing the clearing cost data. */
@Data
@Entity
@Table(name = "clearing_cost")
public class ClearingCost {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "country_code", nullable = false, unique = true)
  @NotNull(message = "Country code cannot be null")
  @Size(min = 2, max = 2, message = "Country code must be exactly 2 characters")
  private String countryCode;

  @Column(name = "cost", nullable = false)
  @NotNull(message = "Cost cannot be null")
  @DecimalMin(value = "0.0", message = "Cost must be a positive value")
  private BigDecimal cost;

  public Long getId() {
    return id;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public BigDecimal getCost() {
    return cost;
  }
}
