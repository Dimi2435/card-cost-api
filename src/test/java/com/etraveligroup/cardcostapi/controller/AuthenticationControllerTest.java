package com.etraveligroup.cardcostapi.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.etraveligroup.cardcostapi.dto.AuthenticationRequest;
import com.etraveligroup.cardcostapi.dto.AuthenticationResponse;
import com.etraveligroup.cardcostapi.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.server.ResponseStatusException;

/**
 * Unit tests for AuthenticationController class. This class tests the authentication functionality
 * of the controller.
 *
 * <p>Author: Dimitrios Milios
 */
class AuthenticationControllerTest {

  @Mock private AuthenticationManager authenticationManager;

  @Mock private JwtUtil jwtUtil;

  @Mock private UserDetailsService userDetailsService;

  @InjectMocks private AuthenticationController authenticationController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void authenticate_Success() {
    // Arrange
    AuthenticationRequest request = new AuthenticationRequest("user", "password");
    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn("user");
    when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);
    when(jwtUtil.generateToken("user")).thenReturn("jwt-token");

    // Act
    ResponseEntity<AuthenticationResponse> response =
        authenticationController.authenticate(request);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("jwt-token", response.getBody().getToken());
  }

  @Test
  void authenticate_InvalidCredentials() {
    // Arrange
    AuthenticationRequest request = new AuthenticationRequest("user", "wrong-password");
    when(authenticationManager.authenticate(any()))
        .thenThrow(new BadCredentialsException("Invalid credentials"));

    // Act & Assert
    assertThrows(
        ResponseStatusException.class, () -> authenticationController.authenticate(request));
  }

  @Test
  void authenticate_AuthenticationError() {
    // Arrange
    AuthenticationRequest request = new AuthenticationRequest("user", "password");
    when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Some error"));

    // Act & Assert
    assertThrows(
        ResponseStatusException.class, () -> authenticationController.authenticate(request));
  }
}
