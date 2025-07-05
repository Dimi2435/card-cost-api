package com.etraveligroup.cardcostapi.exception;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** ValidationErrorResponse class for handling validation errors with field-specific messages. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorResponse {
  private String message; // General error message
  private int status; // HTTP status code
  private LocalDateTime timestamp; // Timestamp of the error occurrence
  private Map<String, String> fieldErrors; // Field-specific error messages
}
