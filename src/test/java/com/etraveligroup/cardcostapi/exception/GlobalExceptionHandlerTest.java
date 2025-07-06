package com.etraveligroup.cardcostapi.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.context.request.WebRequest;

/**
 * Unit tests for GlobalExceptionHandler class. This class tests the behavior of the global
 * exception handler for various exceptions.
 *
 * <p>Author: Dimitrios Milios
 */
class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler globalExceptionHandler;

  @Mock private WebRequest webRequest;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    globalExceptionHandler = new GlobalExceptionHandler();
  }

  @Test
  void testHandleInvalidCardNumberException() {
    InvalidCardNumberException exception = new InvalidCardNumberException("Invalid card number");
    ResponseEntity<ErrorResponse> response =
        globalExceptionHandler.handleInvalidCardNumberException(exception, webRequest);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status should be BAD_REQUEST");
    assertEquals(
        "Invalid card number", response.getBody().getMessage(), "Error message should match");
  }

  @Test
  void testHandleCostNotFoundException() {
    CostNotFoundException exception = new CostNotFoundException("Cost not found");
    ResponseEntity<ErrorResponse> response =
        globalExceptionHandler.handleCostNotFoundException(exception, webRequest);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be NOT_FOUND");
    assertEquals("Cost not found", response.getBody().getMessage(), "Error message should match");
  }

  @Test
  void testHandleBadCredentialsException() {
    BadCredentialsException exception = new BadCredentialsException("Invalid credentials");
    ResponseEntity<ErrorResponse> response =
        globalExceptionHandler.handleBadCredentialsException(exception, webRequest);

    assertEquals(
        HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Status should be UNAUTHORIZED");
    assertEquals(
        "Invalid credentials", response.getBody().getMessage(), "Error message should match");
  }

  // @Test
  // void testHandleValidationExceptions() {
  //     MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
  //     ResponseEntity<ValidationErrorResponse> response =
  // globalExceptionHandler.handleValidationExceptions(exception, webRequest);

  //     assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status should be
  // BAD_REQUEST");
  //     assertEquals("Validation failed", response.getBody().getMessage(), "Error message should
  // match");
  // }

  @Test
  void testHandleGenericException() {
    Exception exception = new Exception("Generic error");
    ResponseEntity<ErrorResponse> response =
        globalExceptionHandler.handleGenericException(exception, webRequest);

    assertEquals(
        HttpStatus.INTERNAL_SERVER_ERROR,
        response.getStatusCode(),
        "Status should be INTERNAL_SERVER_ERROR");
    assertEquals(
        "An unexpected error occurred. Please contact support if the problem persists.",
        response.getBody().getMessage(),
        "Error message should match");
  }
}
