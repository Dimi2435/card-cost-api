package com.etraveligroup.cardcostapi.controller;

import com.etraveligroup.cardcostapi.dto.AppUserRegistrationRequest;
import com.etraveligroup.cardcostapi.service.AppUserDetailsService;
import com.etraveligroup.cardcostapi.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
    AppConstants.API_BASE_PATH
        + "/v"
        + AppConstants.DEFAULT_API_VERSION
        + AppConstants.USERS_ENDPOINT)
public class AppUserController {

  @Autowired private AppUserDetailsService appUserDetailsService;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody AppUserRegistrationRequest request) {
    appUserDetailsService.registerUser(request.getUsername(), request.getPassword());
    return ResponseEntity.ok("User registered successfully");
  }
}
