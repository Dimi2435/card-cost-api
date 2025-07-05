package com.etraveligroup.cardcostapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.etraveligroup.cardcostapi.dto.CalculateClearingCostRequest;
import com.etraveligroup.cardcostapi.dto.CreateClearingCostRequest;
import com.etraveligroup.cardcostapi.dto.UpdateClearingCostRequest;
import com.etraveligroup.cardcostapi.model.ClearingCost;
import com.etraveligroup.cardcostapi.repository.ClearingCostRepository;
import com.etraveligroup.cardcostapi.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureWebMvc
@Transactional
@TestPropertySource(
    properties = {
      "app.external.binlist.url=http://localhost:8089/",
      "app.external.binlist.timeout=1000",
      "app.external.binlist.retry-attempts=1"
    })
class ClearingCostControllerIntegrationTest {

  @Autowired private WebApplicationContext webApplicationContext;

  @Autowired private ClearingCostRepository clearingCostRepository;

  @Autowired private JwtUtil jwtUtil;

  @Autowired private ObjectMapper objectMapper;

  private MockMvc mockMvc;
  private String adminToken;
  private String userToken;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    // Generate test tokens
    adminToken = jwtUtil.generateToken("admin");
    userToken = jwtUtil.generateToken("user");

    // Set up test data
    setupTestData();
  }

  private void setupTestData() {
    clearingCostRepository.deleteAll();

    ClearingCost usCost = new ClearingCost();
    usCost.setCountryCode("US");
    usCost.setCost(BigDecimal.valueOf(5.0));
    clearingCostRepository.save(usCost);

    ClearingCost gbCost = new ClearingCost();
    gbCost.setCountryCode("GB");
    gbCost.setCost(BigDecimal.valueOf(7.5));
    clearingCostRepository.save(gbCost);

    ClearingCost othersCost = new ClearingCost();
    othersCost.setCountryCode("OTHERS");
    othersCost.setCost(BigDecimal.valueOf(10.0));
    clearingCostRepository.save(othersCost);
  }

  @Test
  void calculateCost_WithValidRequest_ReturnsSuccess() throws Exception {
    CalculateClearingCostRequest request = new CalculateClearingCostRequest();
    request.setCardNumber("4111111111111111");

    mockMvc
        .perform(
            post("/api/v1/payment-cards-cost")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.country").exists())
        .andExpect(jsonPath("$.cost").exists());
  }

  @Test
  void calculateCost_WithoutToken_ReturnsUnauthorized() throws Exception {
    CalculateClearingCostRequest request = new CalculateClearingCostRequest();
    request.setCardNumber("4111111111111111");

    mockMvc
        .perform(
            post("/api/v1/payment-cards-cost")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void calculateCost_WithInvalidToken_ReturnsUnauthorized() throws Exception {
    CalculateClearingCostRequest request = new CalculateClearingCostRequest();
    request.setCardNumber("4111111111111111");

    mockMvc
        .perform(
            post("/api/v1/payment-cards-cost")
                .header("Authorization", "Bearer invalid_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void calculateCost_WithMalformedJson_ReturnsBadRequest() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/payment-cards-cost")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"cardNumber\": 4111111111111111}")) // Missing quotes
        .andExpect(status().isBadRequest());
  }

  @Test
  void createClearingCost_WithAdminRole_ReturnsSuccess() throws Exception {
    CreateClearingCostRequest request = new CreateClearingCostRequest();
    request.setCountryCode("DE");
    request.setCost(BigDecimal.valueOf(6.0));

    mockMvc
        .perform(
            post("/api/v1/payment-cards-cost/create")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.country").value("DE"))
        .andExpect(jsonPath("$.cost").value(6.0));
  }

  @Test
  void createClearingCost_WithUserRole_ReturnsForbidden() throws Exception {
    CreateClearingCostRequest request = new CreateClearingCostRequest();
    request.setCountryCode("DE");
    request.setCost(BigDecimal.valueOf(6.0));

    mockMvc
        .perform(
            post("/api/v1/payment-cards-cost/create")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden());
  }

  @Test
  void getAllClearingCosts_WithAdminRole_ReturnsSuccess() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/payment-cards-cost/all").header("Authorization", "Bearer " + adminToken))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(3));
  }

  @Test
  void getAllClearingCosts_WithUserRole_ReturnsForbidden() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/payment-cards-cost/all").header("Authorization", "Bearer " + userToken))
        .andExpect(status().isForbidden());
  }

  @Test
  void getAllClearingCostsPaged_WithAdminRole_ReturnsSuccess() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/payment-cards-cost/all/paged")
                .param("page", "0")
                .param("size", "2")
                .param("sortBy", "countryCode")
                .param("sortDirection", "ASC")
                .header("Authorization", "Bearer " + adminToken))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.page").value(0))
        .andExpect(jsonPath("$.size").value(2))
        .andExpect(jsonPath("$.totalElements").value(3))
        .andExpect(jsonPath("$.totalPages").value(2));
  }

  @Test
  void getClearingCostById_WithValidId_ReturnsSuccess() throws Exception {
    ClearingCost cost = clearingCostRepository.findByCountryCode("US").orElseThrow();

    mockMvc
        .perform(
            get("/api/v1/payment-cards-cost/" + cost.getId())
                .header("Authorization", "Bearer " + userToken))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.country").value("US"))
        .andExpect(jsonPath("$.cost").value(5.0));
  }

  @Test
  void getClearingCostById_WithInvalidId_ReturnsNotFound() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/payment-cards-cost/999").header("Authorization", "Bearer " + userToken))
        .andExpect(status().isNotFound());
  }

  @Test
  void updateClearingCost_WithAdminRole_ReturnsSuccess() throws Exception {
    ClearingCost cost = clearingCostRepository.findByCountryCode("US").orElseThrow();
    UpdateClearingCostRequest request = new UpdateClearingCostRequest();
    request.setCost(BigDecimal.valueOf(8.0));

    mockMvc
        .perform(
            put("/api/v1/payment-cards-cost/" + cost.getId())
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.country").value("US"))
        .andExpect(jsonPath("$.cost").value(8.0));
  }

  @Test
  void updateClearingCost_WithUserRole_ReturnsForbidden() throws Exception {
    ClearingCost cost = clearingCostRepository.findByCountryCode("US").orElseThrow();
    UpdateClearingCostRequest request = new UpdateClearingCostRequest();
    request.setCost(BigDecimal.valueOf(8.0));

    mockMvc
        .perform(
            put("/api/v1/payment-cards-cost/" + cost.getId())
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden());
  }

  @Test
  void deleteClearingCost_WithAdminRole_ReturnsSuccess() throws Exception {
    ClearingCost cost = clearingCostRepository.findByCountryCode("GB").orElseThrow();

    mockMvc
        .perform(
            delete("/api/v1/payment-cards-cost/" + cost.getId())
                .header("Authorization", "Bearer " + adminToken))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteClearingCost_WithUserRole_ReturnsForbidden() throws Exception {
    ClearingCost cost = clearingCostRepository.findByCountryCode("GB").orElseThrow();

    mockMvc
        .perform(
            delete("/api/v1/payment-cards-cost/" + cost.getId())
                .header("Authorization", "Bearer " + userToken))
        .andExpect(status().isForbidden());
  }
}
