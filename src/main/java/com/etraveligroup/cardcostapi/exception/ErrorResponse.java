package com.etraveligroup.cardcostapi.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** ErrorResponse class to standardize error responses from the API. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
  private String message; // Error message
  private int status; // HTTP status code
  private LocalDateTime timestamp; // Timestamp of the error occurrence

  public ErrorResponse(String message, int status) {
    this.message = message;
    this.status = status;
    this.timestamp = LocalDateTime.now(); // Set current timestamp
  }
}
