package com.etraveligroup.cardcostapi.controller;

import com.etraveligroup.cardcostapi.dto.AuthRequest;
import com.etraveligroup.cardcostapi.dto.AuthResponse;
import com.etraveligroup.cardcostapi.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication controller for JWT token generation. Provides endpoint for user authentication and
 * JWT token generation.
 */
@RestController
@RequestMapping("/authenticate")
public class AuthController {

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private JwtUtil jwtUtil;

  /**
   * Authenticates user credentials and returns a JWT token.
   *
   * @param authRequest The authentication request containing username and password
   * @return ResponseEntity containing the JWT token and user details
   */
  @Operation(summary = "Authenticate user and generate JWT token")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Authentication successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
      })
  @PostMapping
  public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  authRequest.getUsername(), authRequest.getPassword()));

      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String jwt = jwtUtil.generateToken(userDetails.getUsername());

      return ResponseEntity.ok(
          new AuthResponse(
              jwt, userDetails.getUsername(), userDetails.getAuthorities().toString()));
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("Invalid credentials", e);
    }
  }
}
