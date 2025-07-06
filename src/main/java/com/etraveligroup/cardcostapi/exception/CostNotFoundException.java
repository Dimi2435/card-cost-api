package com.etraveligroup.cardcostapi.exception;

/**
 * Exception thrown when a cost is not found. This exception extends RuntimeException to indicate a
 * business logic error.
 *
 * <p>Author: Dimitrios Milios
 */
public class CostNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public CostNotFoundException(String message) {
    super(message);
  }
}
