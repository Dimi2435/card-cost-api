package com.etraveligroup.cardcostapi.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ClearingCostController.class)
public class ClearingCostControllerTest {

  @Autowired private MockMvc mockMvc;

  @BeforeEach
  public void setUp() throws Exception {
    // Setup code if needed
  }

  @Test
  public void testGetAllClearingCosts() throws Exception {
    mockMvc
        .perform(get("/api/v1/payment-cards-cost/all").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
}
