package com.etraveligroup.cardcostapi.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.etraveligroup.cardcostapi.dto.ClearingCostResponse;
import com.etraveligroup.cardcostapi.dto.CreateClearingCostRequest;
import com.etraveligroup.cardcostapi.dto.PagedResponse;
import com.etraveligroup.cardcostapi.dto.UpdateClearingCostRequest;
import com.etraveligroup.cardcostapi.exception.CostNotFoundException;
import com.etraveligroup.cardcostapi.exception.InvalidCardNumberException;
import com.etraveligroup.cardcostapi.model.ClearingCost;
import com.etraveligroup.cardcostapi.repository.ClearingCostRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class ClearingCostServiceImplTest {

  @Mock private ClearingCostRepository clearingCostRepository;
  @Mock private WebClient binlistWebClient;
  @Mock private RequestHeadersUriSpec requestHeadersUriSpec;
  @Mock private ResponseSpec responseSpec;

  @InjectMocks private ClearingCostServiceImpl clearingCostService;

  private ClearingCost clearingCost;
  private CreateClearingCostRequest createRequest;
  private UpdateClearingCostRequest updateRequest;

  @BeforeEach
  void setUp() {
    // Set up test data
    clearingCost = new ClearingCost();
    clearingCost.setId(1L);
    clearingCost.setCountryCode("US");
    clearingCost.setCost(BigDecimal.valueOf(5.0));

    createRequest = new CreateClearingCostRequest();
    createRequest.setCountryCode("GB");
    createRequest.setCost(BigDecimal.valueOf(7.5));

    updateRequest = new UpdateClearingCostRequest();
    updateRequest.setCost(BigDecimal.valueOf(8.0));

    // Set up service properties
    ReflectionTestUtils.setField(clearingCostService, "binlistTimeout", 1000);
    ReflectionTestUtils.setField(clearingCostService, "retryAttempts", 1);
    ReflectionTestUtils.setField(clearingCostService, "defaultCountryCode", "OTHERS");
    ReflectionTestUtils.setField(clearingCostService, "defaultCost", BigDecimal.valueOf(10.0));
  }

  @Test
  void calculateCardClearingCost_InvalidCardNumber_ThrowsException() {
    // Arrange
    String invalidCardNumber = "12345"; // Less than 6 digits

    // Act & Assert
    InvalidCardNumberException exception =
        assertThrows(
            InvalidCardNumberException.class,
            () -> clearingCostService.calculateCardClearingCost(invalidCardNumber));

    assertTrue(exception.getMessage().contains("must have at least 6 digits"));
  }

  @Test
  void calculateCardClearingCost_NullCardNumber_ThrowsException() {
    // Arrange
    String nullCardNumber = null;

    // Act & Assert
    InvalidCardNumberException exception =
        assertThrows(
            InvalidCardNumberException.class,
            () -> clearingCostService.calculateCardClearingCost(nullCardNumber));

    assertTrue(exception.getMessage().contains("cannot be blank"));
  }

  @Test
  void calculateCardClearingCost_EmptyCardNumber_ThrowsException() {
    // Arrange
    String emptyCardNumber = "";

    // Act & Assert
    InvalidCardNumberException exception =
        assertThrows(
            InvalidCardNumberException.class,
            () -> clearingCostService.calculateCardClearingCost(emptyCardNumber));

    assertTrue(exception.getMessage().contains("cannot be blank"));
  }

  @Test
  void calculateCardClearingCost_ValidCardNumber_WithExternalApiFailure_ReturnsDefaultCost() {
    // Arrange
    String validCardNumber = "4111111111111111";
    String bin = "411111";

    // Mock WebClient to return empty (API failure)
    when(binlistWebClient.get()).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.uri("/" + bin)).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.bodyToMono(any(Class.class))).thenReturn(Mono.empty());

    // Mock repository for default country
    ClearingCost defaultCost = new ClearingCost();
    defaultCost.setCountryCode("OTHERS");
    defaultCost.setCost(BigDecimal.valueOf(10.0));
    when(clearingCostRepository.findByCountryCode("OTHERS")).thenReturn(Optional.of(defaultCost));

    // Act
    ClearingCostResponse response = clearingCostService.calculateCardClearingCost(validCardNumber);

    // Assert
    assertNotNull(response);
    assertEquals("OTHERS", response.getCountry());
    assertEquals(BigDecimal.valueOf(10.0), response.getCost());
  }

  @Test
  void createClearingCost_ValidRequest_ReturnsResponse() {
    // Arrange
    when(clearingCostRepository.findByCountryCode("GB")).thenReturn(Optional.empty());

    ClearingCost savedCost = new ClearingCost();
    savedCost.setId(2L);
    savedCost.setCountryCode("GB");
    savedCost.setCost(BigDecimal.valueOf(7.5));

    when(clearingCostRepository.save(any(ClearingCost.class))).thenReturn(savedCost);

    // Act
    ClearingCostResponse response = clearingCostService.createClearingCost(createRequest);

    // Assert
    assertNotNull(response);
    assertEquals("GB", response.getCountry());
    assertEquals(BigDecimal.valueOf(7.5), response.getCost());
    verify(clearingCostRepository).save(any(ClearingCost.class));
  }

  @Test
  void createClearingCost_DuplicateCountryCode_ThrowsException() {
    // Arrange
    when(clearingCostRepository.findByCountryCode("GB")).thenReturn(Optional.of(clearingCost));

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          clearingCostService.createClearingCost(createRequest);
        });
  }

  @Test
  void getAllClearingCosts_ReturnsAllCosts() {
    // Arrange
    List<ClearingCost> costs = Arrays.asList(clearingCost);
    when(clearingCostRepository.findAll()).thenReturn(costs);

    // Act
    List<ClearingCostResponse> responses = clearingCostService.getAllClearingCosts();

    // Assert
    assertNotNull(responses);
    assertEquals(1, responses.size());
    assertEquals("US", responses.get(0).getCountry());
    assertEquals(BigDecimal.valueOf(5.0), responses.get(0).getCost());
  }

  @Test
  void getAllClearingCosts_WithPagination_ReturnsPagedResponse() {
    // Arrange
    Pageable pageable = PageRequest.of(0, 10);
    List<ClearingCost> costs = Arrays.asList(clearingCost);
    Page<ClearingCost> page = new PageImpl<>(costs, pageable, 1);

    when(clearingCostRepository.findAll(pageable)).thenReturn(page);

    // Act
    PagedResponse<ClearingCostResponse> response =
        clearingCostService.getAllClearingCosts(pageable);

    // Assert
    assertNotNull(response);
    assertEquals(1, response.getContent().size());
    assertEquals(0, response.getPage());
    assertEquals(10, response.getSize());
    assertEquals(1, response.getTotalElements());
    assertEquals(1, response.getTotalPages());
    assertTrue(response.isFirst());
    assertTrue(response.isLast());
  }

  @Test
  void getClearingCostById_ExistingId_ReturnsResponse() {
    // Arrange
    when(clearingCostRepository.findById(1L)).thenReturn(Optional.of(clearingCost));

    // Act
    ClearingCostResponse response = clearingCostService.getClearingCostById(1L);

    // Assert
    assertNotNull(response);
    assertEquals("US", response.getCountry());
    assertEquals(BigDecimal.valueOf(5.0), response.getCost());
  }

  @Test
  void getClearingCostById_NonExistingId_ThrowsException() {
    // Arrange
    when(clearingCostRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        CostNotFoundException.class,
        () -> {
          clearingCostService.getClearingCostById(999L);
        });
  }

  @Test
  void updateClearingCost_ExistingId_ReturnsUpdatedResponse() {
    // Arrange
    when(clearingCostRepository.findById(1L)).thenReturn(Optional.of(clearingCost));
    when(clearingCostRepository.save(clearingCost)).thenReturn(clearingCost);

    // Act
    ClearingCostResponse response = clearingCostService.updateClearingCost(1L, updateRequest);

    // Assert
    assertNotNull(response);
    assertEquals("US", response.getCountry());
    assertEquals(BigDecimal.valueOf(8.0), response.getCost());
    verify(clearingCostRepository).save(clearingCost);
  }

  @Test
  void updateClearingCost_NonExistingId_ThrowsException() {
    // Arrange
    when(clearingCostRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        CostNotFoundException.class,
        () -> {
          clearingCostService.updateClearingCost(999L, updateRequest);
        });
  }

  @Test
  void deleteClearingCost_ExistingId_DeletesSuccessfully() {
    // Arrange
    when(clearingCostRepository.findById(1L)).thenReturn(Optional.of(clearingCost));

    // Act
    clearingCostService.deleteClearingCost(1L);

    // Assert
    verify(clearingCostRepository).delete(clearingCost);
  }

  @Test
  void deleteClearingCost_NonExistingId_ThrowsException() {
    // Arrange
    when(clearingCostRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        CostNotFoundException.class,
        () -> {
          clearingCostService.deleteClearingCost(999L);
        });
  }
}
