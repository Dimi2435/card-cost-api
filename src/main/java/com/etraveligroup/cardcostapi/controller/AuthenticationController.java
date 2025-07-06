package com.etraveligroup.cardcostapi.controller;

import com.etraveligroup.cardcostapi.dto.AuthenticationRequest;
import com.etraveligroup.cardcostapi.dto.AuthenticationResponse;
import com.etraveligroup.cardcostapi.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for handling authentication requests. Provides endpoints for user login and JWT token
 * generation.
 */
@RestController
@RequestMapping("/authenticate")
@Tag(name = "Authentication", description = "Authentication endpoints for JWT token generation")
public class AuthenticationController {

  private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;

  @Autowired
  public AuthenticationController(
      AuthenticationManager authenticationManager,
      JwtUtil jwtUtil,
      UserDetailsService userDetailsService) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
  }

  /**
   * Authenticate user and generate JWT token.
   *
   * @param authenticationRequest The authentication request containing username and password
   * @return ResponseEntity containing the JWT token
   */
  @Operation(summary = "Authenticate user and generate JWT token")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Authentication successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
      })
  @PostMapping
  public ResponseEntity<AuthenticationResponse> authenticate(
      @Valid @RequestBody AuthenticationRequest authenticationRequest) {

    try {
      logger.info("Authentication attempt for user: {}", authenticationRequest.getUsername());

      // Authenticate the user
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  authenticationRequest.getUsername(), authenticationRequest.getPassword()));

      // Load user details
      UserDetails userDetails =
          userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

      // Generate JWT token
      String jwt = jwtUtil.generateToken(userDetails.getUsername());

      logger.info("Authentication successful for user: {}", authenticationRequest.getUsername());

      AuthenticationResponse response = new AuthenticationResponse(jwt);
      return ResponseEntity.ok(response);

    } catch (BadCredentialsException e) {
      logger.warn(
          "Authentication failed for user: {} - Invalid credentials",
          authenticationRequest.getUsername());
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    } catch (Exception e) {
      logger.error(
          "Authentication error for user: {} - {}",
          authenticationRequest.getUsername(),
          e.getMessage());
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Authentication failed", e);
    }
  }
}
