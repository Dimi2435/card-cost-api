package com.etraveligroup.cardcostapi.model.entity;

import jakarta.persistence.*; // For JPA annotations like @Entity, @Table, etc.
import java.math.BigDecimal; // For BigDecimal
import lombok.Data; // For Lombok's @Data annotation

// Author: Dimitrios Milios
// Entity representing the clearing cost data.

/** Entity representing the clearing cost data. */
@Data
@Entity
@Table(name = "clearing_costs")
public class ClearingCostEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "country_code", nullable = false, unique = true)
  private String countryCode;

  @Column(name = "cost", nullable = false)
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
