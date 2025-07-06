// package com.etraveligroup.cardcostapi.util;

// import static org.junit.jupiter.api.Assertions.*;

// import java.util.HashMap;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;

// @SpringBootTest
// class JwtUtilTest {

//   @Autowired private JwtUtil jwtUtil;

//   private String username;
//   private String token;

//   @BeforeEach
//   void setUp() {
//     username = "testUser";
//     token = jwtUtil.generateToken(username);
//   }

//   @Test
//   void testGenerateToken() {
//     assertNotNull(token);
//     assertFalse(token.isEmpty());
//   }

//   @Test
//   void testValidateToken_ValidToken() {
//     assertTrue(jwtUtil.validateToken(token, username));
//   }

//   @Test
//   void testValidateToken_InvalidToken() {
//     String invalidToken = "invalidToken";
//     assertFalse(jwtUtil.validateToken(invalidToken, username));
//   }

//   @Test
//   void testValidateToken_ExpiredToken() {
//     // To test expired token, we would need to manipulate the expiration time.
//     // A workaround is to create a token with a short expiration time for testing.
//     String shortLivedToken = jwtUtil.createToken(new HashMap<>(), username);
//     // Simulate expiration by waiting for the token to expire
//     try {
//       Thread.sleep(1000 * 60 * 1 + 1000); // Wait for 1 minute + 1 second
//     } catch (InterruptedException e) {
//       Thread.currentThread().interrupt();
//     }
//     assertFalse(jwtUtil.validateToken(shortLivedToken, username));
//   }

//   @Test
//   void testExtractUsername() {
//     assertEquals(username, jwtUtil.extractUsername(token));
//   }

//   @Test
//   void testExtractUsername_InvalidToken() {
//     String invalidToken = "invalidToken";
//     assertThrows(Exception.class, () -> jwtUtil.extractUsername(invalidToken));
//   }
// }
