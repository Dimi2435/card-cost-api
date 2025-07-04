package com.etraveligroup.cardcostapi.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConstants {

  public static final String API_BASE_PATH = "/api";

  public static final String PAYMENT_CARDS_COST_ENDPOINT = "/payment-cards-cost";

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

  @Value("${app.default.username}")
  public static String DEFAULT_USERNAME;

  @Value("${app.default.password}")
  public static String DEFAULT_PASSWORD;

  @Value("${app.admin.username}")
  public static String ADMIN_USERNAME;

  @Value("${app.admin.password}")
  public static String ADMIN_PASSWORD;

  // Private constructor to prevent instantiation
  private AppConstants() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
}
