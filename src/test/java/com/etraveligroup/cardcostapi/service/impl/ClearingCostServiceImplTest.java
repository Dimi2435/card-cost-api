package com.etraveligroup.cardcostapi.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.etraveligroup.cardcostapi.dto.ClearingCostResponse;
import com.etraveligroup.cardcostapi.dto.CreateClearingCostRequest;
import com.etraveligroup.cardcostapi.dto.UpdateClearingCostRequest;
import com.etraveligroup.cardcostapi.exception.InvalidCardNumberException;
import com.etraveligroup.cardcostapi.model.ClearingCost;
import com.etraveligroup.cardcostapi.repository.ClearingCostRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for ClearingCostServiceImpl class. This class tests the service methods for managing
 * clearing costs.
 *
 * <p>Author: Dimitrios Milios
 */
class ClearingCostServiceImplTest {

  @InjectMocks private ClearingCostServiceImpl clearingCostService;

  @Mock private ClearingCostRepository clearingCostRepository;

  private static final String COUNTRY_CODE = "US";
  private static final BigDecimal COST = BigDecimal.valueOf(5.00);
  private static final String VALID_CARD_NUMBER = "45717360";
  private static final String INVALID_CARD_NUMBER = "123";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCalculateCardClearingCost_InvalidCardNumber() {
    // Act & Assert
    assertThrows(
        InvalidCardNumberException.class,
        () -> clearingCostService.calculateCardClearingCost(INVALID_CARD_NUMBER));
  }

  @Test
  void testCreateClearingCost() {
    // Arrange
    when(clearingCostRepository.findByCountryCode(COUNTRY_CODE)).thenReturn(Optional.empty());
    ClearingCost newCost = createClearingCost(COUNTRY_CODE, COST);
    when(clearingCostRepository.save(any(ClearingCost.class))).thenReturn(newCost);

    // Act
    ClearingCostResponse response =
        clearingCostService.createClearingCost(new CreateClearingCostRequest(COUNTRY_CODE, COST));

    // Assert
    assertEquals(COUNTRY_CODE, response.getCountry());
    assertEquals(COST, response.getCost());
  }

  @Test
  void testUpdateClearingCost() {
    // Arrange
    ClearingCost existingCost = createClearingCost(COUNTRY_CODE, COST);
    when(clearingCostRepository.findById(1L)).thenReturn(Optional.of(existingCost));

    // Act
    ClearingCostResponse response =
        clearingCostService.updateClearingCost(1L, new UpdateClearingCostRequest(COST));

    // Assert
    assertEquals(COUNTRY_CODE, response.getCountry());
    assertEquals(COST, response.getCost());
  }

  @Test
  void testDeleteClearingCost() {
    // Arrange
    ClearingCost existingCost = createClearingCost(COUNTRY_CODE, COST);
    when(clearingCostRepository.findById(1L)).thenReturn(Optional.of(existingCost));

    // Act
    clearingCostService.deleteClearingCost(1L);

    // Assert
    verify(clearingCostRepository, times(1)).delete(existingCost);
  }

  private ClearingCost createClearingCost(String countryCode, BigDecimal cost) {
    ClearingCost clearingCost = new ClearingCost();
    clearingCost.setCountryCode(countryCode);
    clearingCost.setCost(cost);
    return clearingCost;
  }
}
