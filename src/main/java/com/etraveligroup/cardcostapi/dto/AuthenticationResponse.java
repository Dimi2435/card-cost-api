package com.etraveligroup.cardcostapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO for authentication response containing JWT token. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication response with JWT token")
public class AuthenticationResponse {

  @Schema(description = "JWT token for API authentication", example = "eyJhbGciOiJIUzI1NiJ9...")
  private String token;
}
