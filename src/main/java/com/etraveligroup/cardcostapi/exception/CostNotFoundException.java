package com.etraveligroup.cardcostapi.exception;

public class CostNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public CostNotFoundException(String message) {
    super(message);
  }
}
