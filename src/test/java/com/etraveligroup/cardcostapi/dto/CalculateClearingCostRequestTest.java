// package com.etraveligroup.cardcostapi.dto;

// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.Test;
// import jakarta.validation.Validation;
// import jakarta.validation.Validator;
// import jakarta.validation.ValidatorFactory;
// import jakarta.validation.ConstraintViolation;
// import java.util.Set;

// /**
//  * Unit tests for CalculateClearingCostRequest class.
//  * This class tests the validation of the card clearing cost request.
//  *
//  * Author: Dimitrios Milios
//  */
// class CalculateClearingCostRequestTest {

//     private final Validator validator;

//     public CalculateClearingCostRequestTest() {
//         ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//         validator = factory.getValidator();
//     }

//     @Test
//     void testValidCardNumber() {
//         CalculateClearingCostRequest request = new CalculateClearingCostRequest();
//         request.setCardNumber("45717360");

//         Set<ConstraintViolation<CalculateClearingCostRequest>> violations =
//             validator.validate(request);
//         assertTrue(violations.isEmpty(), "Should be valid with a proper card number");
//     }

//     @Test
//     void testEmptyCardNumber() {
//         CalculateClearingCostRequest request = new CalculateClearingCostRequest();
//         request.setCardNumber("");

//         Set<ConstraintViolation<CalculateClearingCostRequest>> violations =
//             validator.validate(request);
//         assertEquals(1, violations.size(), "Should have one violation for empty card number");
//         assertEquals("Card number cannot be empty", violations.iterator().next().getMessage());
//     }

//     @Test
//     void testCardNumberTooShort() {
//         CalculateClearingCostRequest request = new CalculateClearingCostRequest();
//         request.setCardNumber("1234567");

//         Set<ConstraintViolation<CalculateClearingCostRequest>> violations =
//             validator.validate(request);
//         assertEquals(1, violations.size(), "Should have one violation for card number too
// short");
//         assertEquals("Card number must be between 8 and 19 digits",
//             violations.iterator().next().getMessage());
//     }

//     @Test
//     void testCardNumberTooLong() {
//         CalculateClearingCostRequest request = new CalculateClearingCostRequest();
//         request.setCardNumber("123456789012345678901");

//         Set<ConstraintViolation<CalculateClearingCostRequest>> violations =
//             validator.validate(request);
//         assertEquals(1, violations.size(), "Should have one violation for card number too long");
//         assertEquals("Card number must be between 8 and 19 digits",
//             violations.iterator().next().getMessage());
//     }

//     @Test
//     void testCardNumberInvalidCharacters() {
//         CalculateClearingCostRequest request = new CalculateClearingCostRequest();
//         request.setCardNumber("4571A7360");

//         Set<ConstraintViolation<CalculateClearingCostRequest>> violations =
//             validator.validate(request);
//         assertEquals(1, violations.size(), "Should have one violation for invalid characters in
// card number");
//         assertEquals("Card number must contain only digits",
//             violations.iterator().next().getMessage());
//     }
// }
