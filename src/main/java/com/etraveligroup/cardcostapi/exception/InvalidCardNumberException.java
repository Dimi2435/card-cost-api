package com.etraveligroup.cardcostapi.exception;

public class InvalidCardNumberException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public InvalidCardNumberException(String message) {
    super(message);
  }
}
