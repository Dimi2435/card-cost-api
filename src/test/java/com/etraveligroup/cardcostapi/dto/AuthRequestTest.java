// package com.etraveligroup.cardcostapi.dto;

// import static org.junit.jupiter.api.Assertions.*;

// import org.junit.jupiter.api.Test;
// import org.springframework.validation.annotation.Validated;
// import javax.validation.ConstraintViolation;
// import javax.validation.Validation;
// import javax.validation.Validator;
// import javax.validation.ValidatorFactory;
// import java.util.Set;

// public class AuthRequestTest {

//     private final Validator validator;

//     public AuthRequestTest() {
//         ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//         this.validator = factory.getValidator();
//     }

//     @Test
//     void testValidAuthRequest() {
//         AuthRequest request = new AuthRequest("testUser", "password");
//         Set<ConstraintViolation<AuthRequest>> violations = validator.validate(request);
//         assertTrue(violations.isEmpty(), "Should be valid");
//     }

//     @Test
//     void testNullUsername() {
//         AuthRequest request = new AuthRequest(null, "password");
//         Set<ConstraintViolation<AuthRequest>> violations = validator.validate(request);
//         assertEquals(1, violations.size(), "Should have one violation");
//         assertEquals("Username cannot be blank", violations.iterator().next().getMessage());
//     }

//     @Test
//     void testNullPassword() {
//         AuthRequest request = new AuthRequest("testUser", null);
//         Set<ConstraintViolation<AuthRequest>> violations = validator.validate(request);
//         assertEquals(1, violations.size(), "Should have one violation");
//         assertEquals("Password cannot be blank", violations.iterator().next().getMessage());
//     }

//     @Test
//     void testBlankUsername() {
//         AuthRequest request = new AuthRequest("", "password");
//         Set<ConstraintViolation<AuthRequest>> violations = validator.validate(request);
//         assertEquals(1, violations.size(), "Should have one violation");
//         assertEquals("Username cannot be blank", violations.iterator().next().getMessage());
//     }

//     @Test
//     void testBlankPassword() {
//         AuthRequest request = new AuthRequest("testUser", "");
//         Set<ConstraintViolation<AuthRequest>> violations = validator.validate(request);
//         assertEquals(1, violations.size(), "Should have one violation");
//         assertEquals("Password cannot be blank", violations.iterator().next().getMessage());
//     }
// }
