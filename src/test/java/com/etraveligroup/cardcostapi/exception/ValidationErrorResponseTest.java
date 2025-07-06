package com.etraveligroup.cardcostapi.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ValidationErrorResponse class. This class tests the behavior of the
 * ValidationErrorResponse.
 *
 * <p>Author: Dimitrios Milios
 */
class ValidationErrorResponseTest {

  @Test
  void testValidationErrorResponseConstructor() {
    String message = "Validation failed";
    int status = 400;
    LocalDateTime timestamp = LocalDateTime.now();
    Map<String, String> fieldErrors = new HashMap<>();
    fieldErrors.put("field1", "Field1 is required");
    fieldErrors.put("field2", "Field2 must be a valid email");

    ValidationErrorResponse errorResponse =
        new ValidationErrorResponse(message, status, timestamp, fieldErrors);

    assertEquals(message, errorResponse.getMessage(), "Error message should match");
    assertEquals(status, errorResponse.getStatus(), "HTTP status should match");
    assertEquals(timestamp, errorResponse.getTimestamp(), "Timestamp should match");
    assertEquals(fieldErrors, errorResponse.getFieldErrors(), "Field errors should match");
  }
}
