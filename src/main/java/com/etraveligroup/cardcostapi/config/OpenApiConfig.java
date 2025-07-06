package com.etraveligroup.cardcostapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAPI/Swagger documentation. This class sets up the API documentation with
 * proper security schemes and server information.
 *
 * <p>Author: Dimitrios Milios
 */
@Configuration
public class OpenApiConfig {

  @Value("${app.base.url:http://localhost:8080}")
  private String baseUrl;

  @Value("${spring.application.name:card-cost-api}")
  private String applicationName;

  /**
   * Customizes the OpenAPI configuration for the application.
   *
   * @return an OpenAPI instance with the configured information.
   */
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Card Cost API")
                .version("1.0.0")
                .description(
                    "API for calculating clearing costs for payment cards based on BIN lookup")
                .contact(new Contact().name("Dimitrios Milios")))
        .servers(List.of(new Server().url(baseUrl).description("Local Development Server")))
        .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
        .components(
            new Components()
                .addSecuritySchemes(
                    "BearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description(
                            "JWT token for API authentication. "
                                + "Use the /authenticate endpoint to obtain a token.")));
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public String getApplicationName() {
    return applicationName;
  }
}
