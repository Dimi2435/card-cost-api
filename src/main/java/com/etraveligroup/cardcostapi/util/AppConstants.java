package com.etraveligroup.cardcostapi.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConstants {

  public static String API_BASE_PATH;

  public static String PAYMENT_CARDS_COST_ENDPOINT;

  public static String USERS_ENDPOINT;

  public static String DEFAULT_API_VERSION;

  public static final String VERSIONED_API_PATH = "/v" + DEFAULT_API_VERSION;

  @Value("${api.base.path}")
  public void setApiBasePath(String apiBasePath) {
    API_BASE_PATH = apiBasePath;
  }

  @Value("${api.payment-cards-cost.endpoint}")
  public void setPaymentCardsCostEndpoint(String paymentCardsCostEndpoint) {
    PAYMENT_CARDS_COST_ENDPOINT = paymentCardsCostEndpoint;
  }

  @Value("${app.admin.username}")
  public void setUsersEndpoint(String usersEndpoint) {
    USERS_ENDPOINT = usersEndpoint;
  }

  @Value("${api.default.version}")
  public void setDefaultApiVersion(String defaultApiVersion) {
    DEFAULT_API_VERSION = defaultApiVersion;
  }

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

  public static String getApiBasePath() {
    return API_BASE_PATH;
  }

  public static String getPaymentCardsCostEndpoint() {
    return PAYMENT_CARDS_COST_ENDPOINT;
  }

  public static String getUsersEndpoint() {
    return USERS_ENDPOINT;
  }

  public static String getDefaultApiVersion() {
    return DEFAULT_API_VERSION;
  }

  public static String getVersionedApiPath() {
    return VERSIONED_API_PATH;
  }
}
