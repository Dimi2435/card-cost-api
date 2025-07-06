package com.etraveligroup.cardcostapi.exception;

/**
 * Exception thrown when an invalid card number is provided. This exception extends RuntimeException
 * to indicate a business logic error.
 *
 * <p>Author: Dimitrios Milios
 */
public class InvalidCardNumberException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public InvalidCardNumberException(String message) {
    super(message);
  }
}
