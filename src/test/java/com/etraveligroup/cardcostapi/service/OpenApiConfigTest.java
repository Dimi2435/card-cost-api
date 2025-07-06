package com.etraveligroup.cardcostapi.service;

import static org.junit.jupiter.api.Assertions.*;

import com.etraveligroup.cardcostapi.config.OpenApiConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // Use the test profile to avoid issues with properties
public class OpenApiConfigTest {

  @Autowired private OpenApiConfig openApiConfig;

  @Test
  public void testBaseUrl() {
    assertNotNull(openApiConfig);
    assertEquals("http://localhost:8080", openApiConfig.getBaseUrl());
  }

  @Test
  public void testApplicationName() {
    assertNotNull(openApiConfig);
    assertEquals("card-cost-api", openApiConfig.getApplicationName());
  }

  @Test
  public void testCustomOpenAPI() {
    assertNotNull(openApiConfig.customOpenAPI());
    assertEquals("Card Cost API", openApiConfig.customOpenAPI().getInfo().getTitle());
    assertEquals("1.0.0", openApiConfig.customOpenAPI().getInfo().getVersion());
  }
}
