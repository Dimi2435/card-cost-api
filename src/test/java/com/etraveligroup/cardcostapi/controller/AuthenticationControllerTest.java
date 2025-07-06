package com.etraveligroup.cardcostapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.etraveligroup.cardcostapi.dto.AuthenticationRequest;
import com.etraveligroup.cardcostapi.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthenticationController.class)
@ActiveProfiles("test")
class AuthenticationControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private AuthenticationManager authenticationManager;

  @MockBean private JwtUtil jwtUtil;

  @MockBean private UserDetailsService userDetailsService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @WithMockUser
  void authenticate_ValidCredentials_ReturnsJwtToken() throws Exception {
    // Arrange
    AuthenticationRequest request = new AuthenticationRequest();
    request.setUsername("testuser");
    request.setPassword("password");

    Authentication authentication = mock(Authentication.class);
    UserDetails userDetails = new User("testuser", "password", Collections.emptyList());
    String jwtToken = "jwt.test.token";

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
    when(jwtUtil.generateToken("testuser")).thenReturn(jwtToken);

    // Act & Assert
    mockMvc
        .perform(
            post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.token").value(jwtToken));

    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(userDetailsService).loadUserByUsername("testuser");
    verify(jwtUtil).generateToken("testuser");
  }

  @Test
  @WithMockUser
  void authenticate_InvalidCredentials_ReturnsBadCredentials() throws Exception {
    // Arrange
    AuthenticationRequest request = new AuthenticationRequest();
    request.setUsername("testuser");
    request.setPassword("wrongpassword");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException("Invalid credentials"));

    // Act & Assert
    mockMvc
        .perform(
            post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());

    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(userDetailsService, never()).loadUserByUsername(anyString());
    verify(jwtUtil, never()).generateToken(anyString());
  }

  @Test
  @WithMockUser
  void authenticate_EmptyUsername_ReturnsBadRequest() throws Exception {
    // Arrange
    AuthenticationRequest request = new AuthenticationRequest();
    request.setUsername("");
    request.setPassword("password");

    // Act & Assert
    mockMvc
        .perform(
            post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser
  void authenticate_EmptyPassword_ReturnsBadRequest() throws Exception {
    // Arrange
    AuthenticationRequest request = new AuthenticationRequest();
    request.setUsername("testuser");
    request.setPassword("");

    // Act & Assert
    mockMvc
        .perform(
            post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser
  void authenticate_RuntimeException_ReturnsInternalServerError() throws Exception {
    // Arrange
    AuthenticationRequest request = new AuthenticationRequest();
    request.setUsername("testuser");
    request.setPassword("password");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new RuntimeException("Database connection failed"));

    // Act & Assert
    mockMvc
        .perform(
            post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isInternalServerError());
  }
}
