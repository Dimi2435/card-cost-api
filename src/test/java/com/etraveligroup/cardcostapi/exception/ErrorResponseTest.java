package com.etraveligroup.cardcostapi.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ErrorResponseTest {

  @Test
  void testErrorResponseConstructorWithMessageAndStatus() {
    String message = "Not Found";
    int status = 404;
    ErrorResponse errorResponse = new ErrorResponse(message, status);

    assertEquals(message, errorResponse.getMessage(), "Error message should match");
    assertEquals(status, errorResponse.getStatus(), "HTTP status should match");
    assertNotNull(errorResponse.getTimestamp(), "Timestamp should not be null");
  }

  @Test
  void testErrorResponseConstructorWithAllArgs() {
    String message = "Bad Request";
    int status = 400;
    LocalDateTime timestamp = LocalDateTime.now();
    ErrorResponse errorResponse = new ErrorResponse(message, status, timestamp);

    assertEquals(message, errorResponse.getMessage(), "Error message should match");
    assertEquals(status, errorResponse.getStatus(), "HTTP status should match");
    assertEquals(timestamp, errorResponse.getTimestamp(), "Timestamp should match");
  }
}
