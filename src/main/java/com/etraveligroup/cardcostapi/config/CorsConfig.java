package com.etraveligroup.cardcostapi.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuration class for CORS (Cross-Origin Resource Sharing). Provides configurable CORS settings
 * for API access.
 */
@Configuration
public class CorsConfig {

  @Value("${app.security.cors.allowed-origins}")
  private String allowedOrigins;

  @Value("${app.security.cors.allowed-methods}")
  private String allowedMethods;

  @Value("${app.security.cors.allowed-headers}")
  private String allowedHeaders;

  @Value("${app.security.cors.allow-credentials}")
  private boolean allowCredentials;

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // Configure allowed origins
    if ("*".equals(allowedOrigins)) {
      configuration.addAllowedOriginPattern("*");
    } else {
      configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
    }

    // Configure allowed methods
    configuration.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));

    // Configure allowed headers
    if ("*".equals(allowedHeaders)) {
      configuration.addAllowedHeader("*");
    } else {
      configuration.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));
    }

    configuration.setAllowCredentials(allowCredentials);
    configuration.setMaxAge(3600L); // 1 hour

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
