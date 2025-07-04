package com.etraveligroup.cardcostapi.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConstants {

  public static final String API_BASE_PATH = "/api";

  public static final String PAYMENT_CARDS_COST_ENDPOINT = "/payment-cards-cost";

  public static final String USERS_ENDPOINT = "/users";

  public static final String DEFAULT_API_VERSION = "1";

  // You could also inject the supported versions as a List<String>
  // @Value("#{'${api.supported-versions}'.split(',')}")
  // public List<String> SUPPORTED_API_VERSIONS;

  // Magic strings
  // public static final String DEFAULT_CURRENCY = "USD"; TODO: Investigate if it is actually needed
  // later

  // Default values
  // public static final int DEFAULT_PAGE_SIZE = 20; // TODO: Apply when dealing with pagination

  // // Default values
  // public static final String DEFAULT_COUNTRY_CODE = "US"; // Example if applicable
  // TODO: Check if this is needed for unit tests

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
