// package com.etraveligroup.cardcostapi.config;

// import static org.junit.jupiter.api.Assertions.*;

// import com.etraveligroup.cardcostapi.config.OpenApiConfig;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;

// /**
//  * Unit tests for OpenApiConfig class.
//  * This class tests the configuration of OpenAPI documentation settings.
//  *
//  * Author: Dimitrios Milios
//  */
// @SpringBootTest
// @ActiveProfiles("test") // Use the test profile to avoid issues with properties
// public class OpenApiConfigTest {

//   @Autowired private OpenApiConfig openApiConfig;

//   @Test
//   public void testBaseUrl() {
//     assertNotNull(openApiConfig);
//     assertEquals("http://localhost:8080", openApiConfig.getBaseUrl());
//   }

//   @Test
//   public void testApplicationName() {
//     assertNotNull(openApiConfig);
//     assertEquals("card-cost-api", openApiConfig.getApplicationName());
//   }

//   @Test
//   public void testCustomOpenAPI() {
//     assertNotNull(openApiConfig.customOpenAPI());
//     assertEquals("Card Cost API", openApiConfig.customOpenAPI().getInfo().getTitle());
//     assertEquals("1.0.0", openApiConfig.customOpenAPI().getInfo().getVersion());
//     assertEquals("http://localhost:8080",
// openApiConfig.customOpenAPI().getServers().get(0).getUrl());
//   }

//   @Test
//   public void testSecurityScheme() {
//     assertNotNull(openApiConfig.customOpenAPI().getComponents());
//     assertNotNull(openApiConfig.customOpenAPI().getComponents().getSecuritySchemes());
//
// assertTrue(openApiConfig.customOpenAPI().getComponents().getSecuritySchemes().containsKey("BearerAuth"));
//   }

//   @Test
//   public void testContactInformation() {
//     assertNotNull(openApiConfig.customOpenAPI().getInfo().getContact());
//     assertEquals("Dimitrios Milios",
// openApiConfig.customOpenAPI().getInfo().getContact().getName());
//   }

//   @Test
//   public void testOpenApiInfoNotNull() {
//     assertNotNull(openApiConfig.customOpenAPI().getInfo());
//   }

//   @Test
//   public void testOpenApiServersNotEmpty() {
//     assertFalse(openApiConfig.customOpenAPI().getServers().isEmpty());
//   }

//   @Test
//   public void testOpenApiComponentsNotNull() {
//     assertNotNull(openApiConfig.customOpenAPI().getComponents());
//   }
// }
