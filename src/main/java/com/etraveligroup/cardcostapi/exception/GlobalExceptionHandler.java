package com.etraveligroup.cardcostapi.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientException;

/**
 * Global exception handler for the application. This class handles various exceptions and returns
 * standardized error responses.
 *
 * <p>Author: Dimitrios Milios
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(InvalidCardNumberException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleInvalidCardNumberException(
      InvalidCardNumberException ex, WebRequest request) {
    logger.warn("Invalid card number provided: {}", ex.getMessage());
    ErrorResponse errorResponse =
        new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(CostNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> handleCostNotFoundException(
      CostNotFoundException ex, WebRequest request) {
    logger.warn("Cost not found: {}", ex.getMessage());
    ErrorResponse errorResponse =
        new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<ErrorResponse> handleBadCredentialsException(
      BadCredentialsException ex, WebRequest request) {
    logger.warn("Authentication failed: {}", ex.getMessage());
    ErrorResponse errorResponse =
        new ErrorResponse(
            "Invalid credentials", HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex, WebRequest request) {
    logger.warn("Validation error: {}", ex.getMessage());
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });

    ValidationErrorResponse errorResponse =
        new ValidationErrorResponse(
            "Validation failed", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), errors);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(WebClientException.class)
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  public ResponseEntity<ErrorResponse> handleWebClientException(
      WebClientException ex, WebRequest request) {
    logger.error("External service unavailable: {}", ex.getMessage());
    ErrorResponse errorResponse =
        new ErrorResponse(
            "External service temporarily unavailable",
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
      IllegalArgumentException ex, WebRequest request) {
    logger.warn("Invalid argument: {}", ex.getMessage());
    ErrorResponse errorResponse =
        new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleMalformedJsonException(
      HttpMessageNotReadableException ex, WebRequest request) {
    logger.warn("Malformed JSON request: {}", ex.getMessage());
    ErrorResponse errorResponse =
        new ErrorResponse(
            "Malformed JSON request. Please check your request format.",
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<ErrorResponse> handleAuthenticationException(
      AuthenticationException ex, WebRequest request) {
    logger.warn("Authentication failed: {}", ex.getMessage());
    ErrorResponse errorResponse =
        new ErrorResponse(
            "Authentication failed. Please provide valid credentials.",
            HttpStatus.UNAUTHORIZED.value(),
            LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(
      AccessDeniedException ex, WebRequest request) {
    logger.warn("Access denied: {}", ex.getMessage());
    ErrorResponse errorResponse =
        new ErrorResponse(
            "Access denied. You don't have permission to access this resource.",
            HttpStatus.FORBIDDEN.value(),
            LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponse> handleNullPointerException(
      NullPointerException ex, WebRequest request) {
    logger.error("Null pointer exception occurred", ex);
    ErrorResponse errorResponse =
        new ErrorResponse(
            "An internal error occurred while processing your request. Please try again or contact support.",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
    logger.error("Unexpected error occurred", ex);
    ErrorResponse errorResponse =
        new ErrorResponse(
            "An unexpected error occurred. Please contact support if the problem persists.",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
