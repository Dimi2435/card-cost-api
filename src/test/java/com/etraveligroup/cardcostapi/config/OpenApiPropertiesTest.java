// package com.etraveligroup.cardcostapi.config;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import static org.junit.jupiter.api.Assertions.*;

// class OpenApiPropertiesTest {

//     private OpenApiProperties openApiProperties;

//     @BeforeEach
//     void setUp() {
//         openApiProperties = new OpenApiProperties();
//     }

//     @Test
//     void defaultValues_AreSetCorrectly() {
//         // Assert
//         assertEquals("http://localhost:8080", openApiProperties.getBaseUrl());
//         assertEquals("card-cost-api", openApiProperties.getApplicationName());
//         assertEquals("Card Cost API", openApiProperties.getTitle());
//         assertEquals("1.0.0", openApiProperties.getVersion());
//         assertEquals("API for calculating clearing costs for payment cards based on BIN lookup",
//                      openApiProperties.getDescription());
//         assertEquals("https://api.etraveligroup.com",
// openApiProperties.getProductionServerUrl());
//     }

//     @Test
//     void setAndGetBaseUrl() {
//         // Arrange
//         String baseUrl = "http://test.example.com";

//         // Act
//         openApiProperties.setBaseUrl(baseUrl);

//         // Assert
//         assertEquals(baseUrl, openApiProperties.getBaseUrl());
//     }

//     @Test
//     void setAndGetApplicationName() {
//         // Arrange
//         String applicationName = "test-api";

//         // Act
//         openApiProperties.setApplicationName(applicationName);

//         // Assert
//         assertEquals(applicationName, openApiProperties.getApplicationName());
//     }

//     @Test
//     void contactDefaults_AreSetCorrectly() {
//         // Assert
//         OpenApiProperties.Contact contact = openApiProperties.getContact();
//         assertNotNull(contact);
//         assertEquals("Development Team", contact.getName());
//         assertEquals("dev@etraveligroup.com", contact.getEmail());
//         assertEquals("https://etraveligroup.com", contact.getUrl());
//     }

//     @Test
//     void licenseDefaults_AreSetCorrectly() {
//         // Assert
//         OpenApiProperties.License license = openApiProperties.getLicense();
//         assertNotNull(license);
//         assertEquals("MIT License", license.getName());
//         assertEquals("https://opensource.org/licenses/MIT", license.getUrl());
//     }

//     @Test
//     void setContact_UpdatesContact() {
//         // Arrange
//         OpenApiProperties.Contact newContact = new OpenApiProperties.Contact();
//         newContact.setName("Test Team");
//         newContact.setEmail("test@example.com");
//         newContact.setUrl("https://test.example.com");

//         // Act
//         openApiProperties.setContact(newContact);

//         // Assert
//         assertEquals("Test Team", openApiProperties.getContact().getName());
//         assertEquals("test@example.com", openApiProperties.getContact().getEmail());
//         assertEquals("https://test.example.com", openApiProperties.getContact().getUrl());
//     }

//     @Test
//     void setLicense_UpdatesLicense() {
//         // Arrange
//         OpenApiProperties.License newLicense = new OpenApiProperties.License();
//         newLicense.setName("Apache License");
//         newLicense.setUrl("https://apache.org/licenses/LICENSE-2.0");

//         // Act
//         openApiProperties.setLicense(newLicense);

//         // Assert
//         assertEquals("Apache License", openApiProperties.getLicense().getName());
//         assertEquals("https://apache.org/licenses/LICENSE-2.0",
// openApiProperties.getLicense().getUrl());
//     }
// }
