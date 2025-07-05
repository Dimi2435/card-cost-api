package com.etraveligroup.cardcostapi.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConstants {

  public static final String API_BASE_PATH = "/api";

  public static final String PAYMENT_CARDS_COST_ENDPOINT = "/payment-cards-cost";

  public static final String USERS_ENDPOINT = "/users";

  public static final String DEFAULT_API_VERSION = "1";

  private static String DEFAULT_USERNAME;
  private static String DEFAULT_PASSWORD;
  private static String ADMIN_USERNAME;
  private static String ADMIN_PASSWORD;

  @Value("${app.default.username}")
  public void setDefaultUsername(String defaultUsername) {
    DEFAULT_USERNAME = defaultUsername;
  }

  @Value("${app.default.password}")
  public void setDefaultPassword(String defaultPassword) {
    DEFAULT_PASSWORD = defaultPassword;
  }

  @Value("${app.admin.username}")
  public void setAdminUsername(String adminUsername) {
    ADMIN_USERNAME = adminUsername;
  }

  @Value("${app.admin.password}")
  public void setAdminPassword(String adminPassword) {
    ADMIN_PASSWORD = adminPassword;
  }

  // Getters for the properties
  public static String getDefaultUsername() {
    return DEFAULT_USERNAME;
  }

  public static String getDefaultPassword() {
    return DEFAULT_PASSWORD;
  }

  public static String getAdminUsername() {
    return ADMIN_USERNAME;
  }

  public static String getAdminPassword() {
    return ADMIN_PASSWORD;
  }
}
