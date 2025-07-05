package com.etraveligroup.cardcostapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication request payload. Contains username and password for user authentication.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

  @Schema(description = "Username for authentication")
  @NotBlank(message = "Username cannot be blank")
  private String username;

  @Schema(description = "Password for authentication")
  @NotBlank(message = "Password cannot be blank")
  private String password;
}
