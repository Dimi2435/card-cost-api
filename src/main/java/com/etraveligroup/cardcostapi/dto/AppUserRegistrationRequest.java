package com.etraveligroup.cardcostapi.dto;

import javax.validation.constraints.NotBlank;

public class AppUserRegistrationRequest {

  @NotBlank(message = "Username is required")
  private String username;

  @NotBlank(message = "Password is required")
  private String password;

  // Default constructor
  public AppUserRegistrationRequest() {}

  // Getters and Setters
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
