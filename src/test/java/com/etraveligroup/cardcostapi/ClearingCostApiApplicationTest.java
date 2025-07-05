package com.etraveligroup.cardcostapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/** Simple context load test to verify all beans are properly configured. */
@SpringBootTest
@TestPropertySource(
    properties = {
      "app.external.binlist.url=http://localhost:8089/",
      "app.external.binlist.timeout=1000",
      "app.external.binlist.retry-attempts=1"
    })
class ClearingCostApiApplicationTest {

  @Test
  void contextLoads() {
    // This test will pass if the application context loads successfully
    // It verifies that all beans are properly configured and dependencies are satisfied
  }
}
