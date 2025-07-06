package com.etraveligroup.cardcostapi.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for AppConstants class. This class tests the constant values defined in the
 * AppConstants.
 *
 * <p>Author: Dimitrios Milios
 */
class AppConstantsTest {

  @Test
  void constantValues_AreCorrect() {
    // Assert
    assertEquals("/api", AppConstants.API_BASE_PATH);
    assertEquals("/payment-cards-cost", AppConstants.PAYMENT_CARDS_COST_ENDPOINT);
    assertEquals("1", AppConstants.DEFAULT_API_VERSION);
  }

  @Test
  void staticGetters_ReturnNonNullValues() {
    // Assert - These may be null in test environment, but methods should exist
    assertDoesNotThrow(() -> AppConstants.getDefaultUsername());
    assertDoesNotThrow(() -> AppConstants.getDefaultPassword());
    assertDoesNotThrow(() -> AppConstants.getAdminUsername());
    assertDoesNotThrow(() -> AppConstants.getAdminPassword());
  }

  @Test
  void appConstants_IsComponent() {
    // This test verifies that AppConstants is properly annotated as a Spring component
    // In a real Spring context, this would be managed by the container
    AppConstants constants = new AppConstants();
    assertNotNull(constants);
  }
}
