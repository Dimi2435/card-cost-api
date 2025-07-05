package com.etraveligroup.cardcostapi.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.etraveligroup.cardcostapi.dto.CreateClearingCostRequest;
import com.etraveligroup.cardcostapi.dto.UpdateClearingCostRequest;
import com.etraveligroup.cardcostapi.exception.InvalidCardNumberException;
import com.etraveligroup.cardcostapi.model.ClearingCost;
import com.etraveligroup.cardcostapi.repository.ClearingCostRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

@ExtendWith(MockitoExtension.class)
class ClearingCostServiceSimpleTest {

  @Mock private ClearingCostRepository clearingCostRepository;
  @Mock private WebClient binlistWebClient;

  @InjectMocks private ClearingCostServiceImpl clearingCostService;

  @BeforeEach
  void setUp() {
    // Set up service properties
    ReflectionTestUtils.setField(clearingCostService, "binlistTimeout", 1000);
    ReflectionTestUtils.setField(clearingCostService, "retryAttempts", 1);
    ReflectionTestUtils.setField(clearingCostService, "defaultCountryCode", "OTHERS");
    ReflectionTestUtils.setField(clearingCostService, "defaultCost", BigDecimal.valueOf(10.0));
  }

  @Test
  void calculateCardClearingCost_InvalidCardNumber_TooShort() {
    // Test with card number too short
    assertThrows(
        InvalidCardNumberException.class,
        () -> clearingCostService.calculateCardClearingCost("12345"));
  }

  @Test
  void calculateCardClearingCost_InvalidCardNumber_Null() {
    // Test with null card number
    assertThrows(
        InvalidCardNumberException.class,
        () -> clearingCostService.calculateCardClearingCost(null));
  }

  @Test
  void calculateCardClearingCost_InvalidCardNumber_Empty() {
    // Test with empty card number
    assertThrows(
        InvalidCardNumberException.class, () -> clearingCostService.calculateCardClearingCost(""));
  }

  @Test
  void createClearingCost_ValidRequest_Success() {
    // Arrange
    CreateClearingCostRequest request = new CreateClearingCostRequest();
    request.setCountryCode("DE");
    request.setCost(BigDecimal.valueOf(6.50));

    when(clearingCostRepository.findByCountryCode("DE")).thenReturn(Optional.empty());

    ClearingCost savedCost = new ClearingCost();
    savedCost.setId(1L);
    savedCost.setCountryCode("DE");
    savedCost.setCost(BigDecimal.valueOf(6.50));

    when(clearingCostRepository.save(any(ClearingCost.class))).thenReturn(savedCost);

    // Act
    var response = clearingCostService.createClearingCost(request);

    // Assert
    assertNotNull(response);
    assertEquals("DE", response.getCountry());
    assertEquals(BigDecimal.valueOf(6.50), response.getCost());
  }

  @Test
  void createClearingCost_DuplicateCountry_ThrowsException() {
    // Arrange
    CreateClearingCostRequest request = new CreateClearingCostRequest();
    request.setCountryCode("US");
    request.setCost(BigDecimal.valueOf(5.00));

    ClearingCost existingCost = new ClearingCost();
    existingCost.setCountryCode("US");
    existingCost.setCost(BigDecimal.valueOf(5.00));

    when(clearingCostRepository.findByCountryCode("US")).thenReturn(Optional.of(existingCost));

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> clearingCostService.createClearingCost(request));
  }

  @Test
  void updateClearingCost_ValidRequest_Success() {
    // Arrange
    Long id = 1L;
    UpdateClearingCostRequest request = new UpdateClearingCostRequest();
    request.setCost(BigDecimal.valueOf(8.00));

    ClearingCost existingCost = new ClearingCost();
    existingCost.setId(id);
    existingCost.setCountryCode("US");
    existingCost.setCost(BigDecimal.valueOf(5.00));

    when(clearingCostRepository.findById(id)).thenReturn(Optional.of(existingCost));
    when(clearingCostRepository.save(existingCost)).thenReturn(existingCost);

    // Act
    var response = clearingCostService.updateClearingCost(id, request);

    // Assert
    assertNotNull(response);
    assertEquals("US", response.getCountry());
    assertEquals(BigDecimal.valueOf(8.00), response.getCost());
    assertEquals(BigDecimal.valueOf(8.00), existingCost.getCost());
  }
}
