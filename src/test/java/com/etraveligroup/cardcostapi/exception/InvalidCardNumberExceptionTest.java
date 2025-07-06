package com.etraveligroup.cardcostapi.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for InvalidCardNumberException class. This class tests the behavior of the
 * InvalidCardNumberException.
 *
 * <p>Author: Dimitrios Milios
 */
class InvalidCardNumberExceptionTest {

  @Test
  void testInvalidCardNumberExceptionMessage() {
    String message = "Invalid card number provided";
    InvalidCardNumberException exception = new InvalidCardNumberException(message);

    assertEquals(message, exception.getMessage(), "Exception message should match");
  }
}
