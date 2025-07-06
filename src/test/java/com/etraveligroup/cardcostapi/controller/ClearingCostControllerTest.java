// package com.etraveligroup.cardcostapi.controller;

// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// import com.etraveligroup.cardcostapi.dto.CalculateClearingCostRequest;
// import com.etraveligroup.cardcostapi.dto.ClearingCostResponse;
// import com.etraveligroup.cardcostapi.dto.CreateClearingCostRequest;
// import com.etraveligroup.cardcostapi.dto.UpdateClearingCostRequest;
// import com.etraveligroup.cardcostapi.service.ClearingCostService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import java.math.BigDecimal;
// import java.util.Collections;

// @WebMvcTest(ClearingCostController.class)
// public class ClearingCostControllerTest {

//   @Autowired private MockMvc mockMvc;

//   @Autowired private ClearingCostService clearingCostService;

//   @BeforeEach
//   public void setUp() throws Exception {
//     // Mocking the service layer
//     clearingCostService = mock(ClearingCostService.class);
//   }

//   @Test
//   public void testCalculateCost_ValidRequest() throws Exception {
//     // Arrange
//     CalculateClearingCostRequest request = new CalculateClearingCostRequest();
//     request.setCardNumber("45717360");
//     ClearingCostResponse response = new ClearingCostResponse();
//     response.setCountry("US");
//     response.setCost(BigDecimal.valueOf(5.00));
//
// when(clearingCostService.calculateCardClearingCost(request.getCardNumber())).thenReturn(response);

//     // Act & Assert
//     mockMvc.perform(
//             post("/api/v1/payment-cards-cost")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"cardNumber\":\"45717360\"}")
//         )
//         .andExpect(status().isOk())
//         .andExpect(jsonPath("$.country").value("US"))
//         .andExpect(jsonPath("$.cost").value(5.00));
//   }

//   @Test
//   public void testCalculateCost_InvalidRequest() throws Exception {
//     // Act & Assert
//     mockMvc.perform(
//             post("/api/v1/payment-cards-cost")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"cardNumber\":\"\"}")
//         )
//         .andExpect(status().isBadRequest());
//   }

//   @Test
//   public void testCreateClearingCost_ValidRequest() throws Exception {
//     // Arrange
//     CreateClearingCostRequest request = new CreateClearingCostRequest();
//     request.setCountryCode("US");
//     request.setCost(BigDecimal.valueOf(5.00));
//     ClearingCostResponse response = new ClearingCostResponse();
//     response.setCountry("US");
//     response.setCost(BigDecimal.valueOf(5.00));
//     when(clearingCostService.createClearingCost(request)).thenReturn(response);

//     // Act & Assert
//     mockMvc.perform(
//             post("/api/v1/payment-cards-cost/create")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"countryCode\":\"US\", \"cost\":5.00}")
//         )
//         .andExpect(status().isCreated())
//         .andExpect(jsonPath("$.country").value("US"))
//         .andExpect(jsonPath("$.cost").value(5.00));
//   }

//   @Test
//   public void testGetAllClearingCosts() throws Exception {
//     // Arrange
//     ClearingCostResponse response = new ClearingCostResponse();
//     response.setCountry("US");
//     response.setCost(BigDecimal.valueOf(5.00));
//
// when(clearingCostService.getAllClearingCosts()).thenReturn(Collections.singletonList(response));

//     // Act & Assert
//     mockMvc.perform(
//             get("/api/v1/payment-cards-cost/all")
//                 .contentType(MediaType.APPLICATION_JSON)
//         )
//         .andExpect(status().isOk())
//         .andExpect(jsonPath("$[0].country").value("US"))
//         .andExpect(jsonPath("$[0].cost").value(5.00));
//   }

//   @Test
//   public void testGetClearingCostById_ValidId() throws Exception {
//     // Arrange
//     Long id = 1L;
//     ClearingCostResponse response = new ClearingCostResponse();
//     response.setCountry("US");
//     response.setCost(BigDecimal.valueOf(5.00));
//     when(clearingCostService.getClearingCostById(id)).thenReturn(response);

//     // Act & Assert
//     mockMvc.perform(
//             get("/api/v1/payment-cards-cost/{id}", id)
//                 .contentType(MediaType.APPLICATION_JSON)
//         )
//         .andExpect(status().isOk())
//         .andExpect(jsonPath(".country").value("US"))
//         .andExpect(jsonPath(".cost").value(5.00));
//   }

//   @Test
//   public void testUpdateClearingCost_ValidRequest() throws Exception {
//     // Arrange
//     Long id = 1L;
//     UpdateClearingCostRequest request = new UpdateClearingCostRequest(BigDecimal.valueOf(10.00));
//     ClearingCostResponse response = new ClearingCostResponse();
//     response.setCountry("US");
//     response.setCost(BigDecimal.valueOf(10.00));
//     when(clearingCostService.updateClearingCost(id, request)).thenReturn(response);

//     // Act & Assert
//     mockMvc.perform(
//             put("/api/v1/payment-cards-cost/{id}", id)
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"cost\":10.00}")
//         )
//         .andExpect(status().isOk())
//         .andExpect(jsonPath(".country").value("US"))
//         .andExpect(jsonPath(".cost").value(10.00));
//   }

//   @Test
//   public void testDeleteClearingCost() throws Exception {
//     // Arrange
//     Long id = 1L;

//     // Act & Assert
//     mockMvc.perform(
//             delete("/api/v1/payment-cards-cost/{id}", id)
//                 .contentType(MediaType.APPLICATION_JSON)
//         )
//         .andExpect(status().isNoContent());
//   }
// }
