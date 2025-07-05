package com.etraveligroup.cardcostapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO for authentication request containing username and password. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication request with username and password")
public class AuthenticationRequest {

  @Schema(description = "Username for authentication", example = "user")
  @NotBlank(message = "Username is required")
  private String username;

  @Schema(description = "Password for authentication", example = "password")
  @NotBlank(message = "Password is required")
  private String password;
}
