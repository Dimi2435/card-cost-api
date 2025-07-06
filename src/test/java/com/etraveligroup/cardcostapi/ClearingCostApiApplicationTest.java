// package com.etraveligroup.cardcostapi;

// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.context.ApplicationContext;
// import static org.assertj.core.api.Assertions.assertThat;

// /** Simple context load test to verify all beans are properly configured. */
// @SpringBootTest
// @TestPropertySource(
//     properties = {
//       "app.external.binlist.url=http://localhost:8089/",
//       "app.external.binlist.timeout=1000",
//       "app.external.binlist.retry-attempts=1"
//     })
// class ClearingCostApiApplicationTest {

//   @Autowired
//   private ApplicationContext applicationContext;

//   @Test
//   void contextLoads() {
//     // This test will pass if the application context loads successfully
//     // It verifies that all beans are properly configured and dependencies are satisfied
//   }

//   @Test
//   void testApplicationContext() {
//     // Verify that the application context loads and the main application class is present
//     assertThat(applicationContext).isNotNull();
//     assertThat(applicationContext.getBean(ClearingCostApiApplication.class)).isNotNull();
//   }
// }
