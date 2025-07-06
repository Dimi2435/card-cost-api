package com.etraveligroup.cardcostapi.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CostNotFoundExceptionTest {

  @Test
  void constructor_WithMessage_SetsMessage() {
    // Arrange
    String message = "Cost not found for ID: 123";

    // Act
    CostNotFoundException exception = new CostNotFoundException(message);

    // Assert
    assertEquals(message, exception.getMessage());
  }

  @Test
  void constructor_WithNullMessage_HandlesNull() {
    // Act
    CostNotFoundException exception = new CostNotFoundException(null);

    // Assert
    assertNull(exception.getMessage());
  }

  @Test
  void exceptionIsRuntimeException() {
    // Arrange
    CostNotFoundException exception = new CostNotFoundException("Test");

    // Assert
    assertTrue(exception instanceof RuntimeException);
  }
}
