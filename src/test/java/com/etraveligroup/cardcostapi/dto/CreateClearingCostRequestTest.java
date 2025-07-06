// package com.etraveligroup.cardcostapi.dto;

// import static org.junit.jupiter.api.Assertions.*;

// import jakarta.validation.ConstraintViolation;
// import jakarta.validation.Validation;
// import jakarta.validation.Validator;
// import jakarta.validation.ValidatorFactory;
// import java.math.BigDecimal;
// import java.util.Set;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

/**
 * Unit tests for CreateClearingCostRequest class. This class tests the validation of the clearing
 * cost creation request.
 *
 * <p>Author: Dimitrios Milios
 */

// class CreateClearingCostRequestTest {

//   private Validator validator;
//   private CreateClearingCostRequest request;

//   @BeforeEach
//   void setUp() {
//     ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//     validator = factory.getValidator();
//     request = new CreateClearingCostRequest();
//   }

//   @Test
//   void setAndGetCountryCode() {
//     // Act
//     request.setCountryCode("US");

//     // Assert
//     assertEquals("US", request.getCountryCode());
//   }

//   @Test
//   void setAndGetCost() {
//     // Arrange
//     BigDecimal cost = BigDecimal.valueOf(5.00);

//     // Act
//     request.setCost(cost);

//     // Assert
//     assertEquals(cost, request.getCost());
//   }

//   @Test
//   void validation_ValidRequest_NoViolations() {
//     // Arrange
//     request.setCountryCode("US");
//     request.setCost(BigDecimal.valueOf(5.00));

//     // Act
//     Set<ConstraintViolation<CreateClearingCostRequest>> violations = validator.validate(request);

//     // Assert
//     assertTrue(violations.isEmpty());
//   }

//   @Test
//   void validation_BlankCountryCode_HasViolation() {
//     // Arrange
//     request.setCountryCode("");
//     request.setCost(BigDecimal.valueOf(5.00));

//     // Act
//     Set<ConstraintViolation<CreateClearingCostRequest>> violations = validator.validate(request);

//     // Assert
//     assertFalse(violations.isEmpty());
//     assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Country code")));
//   }

//   @Test
//   void validation_NullCost_HasViolation() {
//     // Arrange
//     request.setCountryCode("US");
//     request.setCost(null);

//     // Act
//     Set<ConstraintViolation<CreateClearingCostRequest>> violations = validator.validate(request);

//     // Assert
//     assertFalse(violations.isEmpty());
//     assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Cost")));
//   }

//   @Test
//   void validation_NegativeCost_HasViolation() {
//     // Arrange
//     request.setCountryCode("US");
//     request.setCost(BigDecimal.valueOf(-1.00));

//     // Act
//     Set<ConstraintViolation<CreateClearingCostRequest>> violations = validator.validate(request);

//     // Assert
//     assertFalse(violations.isEmpty());
//     assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("greater than zero")));
//   }
// }
