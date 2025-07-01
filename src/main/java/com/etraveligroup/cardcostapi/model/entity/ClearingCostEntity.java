package com.etraveligroup.cardcostapi.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data; // Requires Lombok
import lombok.NoArgsConstructor; // Requires Lombok

import java.math.BigDecimal;

@Entity
@Table(name = "clearing_cost")
@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Generates a no-argument constructor
public class ClearingCostEntity {

    @Id // The primary key, typically the country code
    private String countryCode; //[cite_start]// e.g., "US", "GR", "Others" [cite: 2, 19]
    private BigDecimal cost; //[cite_start]// e.g., $5, $15, $10 [cite: 2, 19]

    public ClearingCostEntity(String countryCode, BigDecimal cost) {
        this.countryCode = countryCode;
        this.cost = cost;
    }

    public BigDecimal getCost() {
        return this.cost;
    }
}