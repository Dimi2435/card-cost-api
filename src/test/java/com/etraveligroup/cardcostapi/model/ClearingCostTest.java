package com.etraveligroup.cardcostapi.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClearingCostTest {

  private ClearingCost clearingCost;

  @BeforeEach
  void setUp() {
    clearingCost = new ClearingCost();
  }

  @Test
  void setAndGetId() {
    // Act
    clearingCost.setId(1L);

    // Assert
    assertEquals(1L, clearingCost.getId());
  }

  @Test
  void setAndGetCountryCode() {
    // Act
    clearingCost.setCountryCode("US");

    // Assert
    assertEquals("US", clearingCost.getCountryCode());
  }

  @Test
  void setAndGetCost() {
    // Arrange
    BigDecimal cost = BigDecimal.valueOf(5.00);

    // Act
    clearingCost.setCost(cost);

    // Assert
    assertEquals(cost, clearingCost.getCost());
  }

  @Test
  void constructor_DefaultValues() {
    // Assert
    assertNull(clearingCost.getId());
    assertNull(clearingCost.getCountryCode());
    assertNull(clearingCost.getCost());
  }

  @Test
  void setAllFields_ValidValues() {
    // Arrange
    String countryCode = "FR";
    BigDecimal cost = BigDecimal.valueOf(12.50);

    // Act
    clearingCost.setCountryCode(countryCode);
    clearingCost.setCost(cost);

    // Assert
    assertEquals(countryCode, clearingCost.getCountryCode());
    assertEquals(cost, clearingCost.getCost());
  }

  @Test
  void equals_SameObject_ReturnsTrue() {
    // Assert
    assertEquals(clearingCost, clearingCost);
  }

  @Test
  void equals_NullObject_ReturnsFalse() {
    // Assert
    assertNotEquals(clearingCost, null);
  }

  @Test
  void equals_DifferentClass_ReturnsFalse() {
    // Assert
    assertNotEquals(clearingCost, "string");
  }

  @Test
  void equals_SameValues_ReturnsTrue() {
    // Arrange
    ClearingCost cost1 = new ClearingCost();
    cost1.setId(1L);
    cost1.setCountryCode("US");
    cost1.setCost(BigDecimal.valueOf(5.00));

    ClearingCost cost2 = new ClearingCost();
    cost2.setId(1L);
    cost2.setCountryCode("US");
    cost2.setCost(BigDecimal.valueOf(5.00));

    // Assert
    assertEquals(cost1, cost2);
  }

  @Test
  void hashCode_SameValues_ReturnsSameHashCode() {
    // Arrange
    ClearingCost cost1 = new ClearingCost();
    cost1.setId(1L);
    cost1.setCountryCode("US");
    cost1.setCost(BigDecimal.valueOf(5.00));

    ClearingCost cost2 = new ClearingCost();
    cost2.setId(1L);
    cost2.setCountryCode("US");
    cost2.setCost(BigDecimal.valueOf(5.00));

    // Assert
    assertEquals(cost1.hashCode(), cost2.hashCode());
  }

  @Test
  void toString_ContainsExpectedValues() {
    // Arrange
    clearingCost.setId(1L);
    clearingCost.setCountryCode("US");
    clearingCost.setCost(BigDecimal.valueOf(5.00));

    // Act
    String result = clearingCost.toString();

    // Assert
    assertNotNull(result);
    assertTrue(result.contains("US"));
    assertTrue(result.contains("5"));
  }
}
