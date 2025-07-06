// package com.etraveligroup.cardcostapi.dto;

// import static org.junit.jupiter.api.Assertions.*;

// import org.junit.jupiter.api.Test;
// import javax.validation.ConstraintViolation;
// import javax.validation.Validation;
// import javax.validation.Validator;
// import javax.validation.ValidatorFactory;
// import java.math.BigDecimal;
// import java.util.Set;

/**
 * Unit tests for UpdateClearingCostRequest class. This class tests the validation of the clearing
 * cost update request.
 *
 * <p>Author: Dimitrios Milios
 */

// public class UpdateClearingCostRequestTest {

//     private final Validator validator;

//     public UpdateClearingCostRequestTest() {
//         ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//         this.validator = factory.getValidator();
//     }

//     @Test
//     void testValidRequest() {
//         UpdateClearingCostRequest request = new UpdateClearingCostRequest(new
// BigDecimal("10.00"));
//         Set<ConstraintViolation<UpdateClearingCostRequest>> violations =
// validator.validate(request);
//         assertTrue(violations.isEmpty(), "Should be valid");
//     }

//     @Test
//     void testNullCost() {
//         UpdateClearingCostRequest request = new UpdateClearingCostRequest(null);
//         Set<ConstraintViolation<UpdateClearingCostRequest>> violations =
// validator.validate(request);
//         assertEquals(1, violations.size(), "Should have one violation");
//         assertEquals("Cost cannot be null", violations.iterator().next().getMessage());
//     }

//     @Test
//     void testNegativeCost() {
//         UpdateClearingCostRequest request = new UpdateClearingCostRequest(new
// BigDecimal("-5.00"));
//         Set<ConstraintViolation<UpdateClearingCostRequest>> violations =
// validator.validate(request);
//         assertEquals(1, violations.size(), "Should have one violation");
//         assertEquals("Cost must be greater than zero",
// violations.iterator().next().getMessage());
//     }

//     @Test
//     void testZeroCost() {
//         UpdateClearingCostRequest request = new UpdateClearingCostRequest(new
// BigDecimal("0.00"));
//         Set<ConstraintViolation<UpdateClearingCostRequest>> violations =
// validator.validate(request);
//         assertEquals(1, violations.size(), "Should have one violation");
//         assertEquals("Cost must be greater than zero",
// violations.iterator().next().getMessage());
//     }
// }
