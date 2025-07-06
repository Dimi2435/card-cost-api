package com.etraveligroup.cardcostapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
 */
@Configuration
public class OpenApiConfig {

  @Value("${app.base.url:http://localhost:8080}")
  private String baseUrl;

  @Value("${spring.application.name:card-cost-api}")
  private String applicationName;

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Card Cost API")
                .version("1.0.0")
                .description(
                    "API for calculating clearing costs for payment cards based on BIN lookup")
                .contact(
                    new Contact()
                        .name("Development Team")
                        .email("dev@etraveligroup.com")
                        .url("https://etraveligroup.com"))
                .license(
                    new License().name("MIT License").url("https://opensource.org/licenses/MIT")))
        .servers(
            List.of(
                new Server().url(baseUrl).description("Local Development Server"),
                new Server().url("https://api.etraveligroup.com").description("Production Server")))
        .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
        .components(
            new Components()
                .addSecuritySchemes(
                    "Bearer Authentication",
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
