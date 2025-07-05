package com.etraveligroup.cardcostapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO for authentication response payload. Contains JWT token and user information. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

  @Schema(description = "JWT token for authentication")
  private String token;

  @Schema(description = "Username of the authenticated user")
  private String username;

  @Schema(description = "Roles/authorities of the authenticated user")
  private String roles;
}
