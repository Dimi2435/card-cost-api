// package com.etraveligroup.cardcostapi.service;

// import com.etraveligroup.cardcostapi.model.entity.ClearingCostEntity;
// import com.etraveligroup.cardcostapi.model.response.CardCostResponse;
// import com.etraveligroup.cardcostapi.repository.ClearingCostRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import reactor.core.publisher.Mono;

// import java.math.BigDecimal;
// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.when;

// // Author: Dimitrios Milios
// // Unit tests for CardCostServiceImpl.

// /**
//  * Unit tests for CardCostServiceImpl.
//  */
// class CardCostServiceImplTest {

//     @Mock
//     private ClearingCostRepository clearingCostRepository;

//     @InjectMocks
//     private CardCostServiceImpl cardCostService;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void testCalculateCardClearingCost_ValidCardNumber() {
//         // Arrange
//         String cardNumber = "4111111111111111"; // Example card number
//         ClearingCostEntity entity = new ClearingCostEntity();
//         entity.setCountryCode("US");
//         entity.setCost(BigDecimal.valueOf(5));

//         when(clearingCostRepository.findByCountryCode("US")).thenReturn(Optional.of(entity));

//         // Act
//         CardCostResponse response = cardCostService.calculateCardClearingCost(cardNumber);

//         // Assert
//         assertNotNull(response);
//         assertEquals("US", response.getCountry());
//         assertEquals(BigDecimal.valueOf(5), response.getCost());
//     }

//     @Test
//     void testCalculateCardClearingCost_InvalidCardNumber() {
//         // Arrange
//         String cardNumber = "12345"; // Invalid card number

//         // Act & Assert
//         assertThrows(IllegalArgumentException.class, () -> {
//             cardCostService.calculateCardClearingCost(cardNumber);
//         });
//     }
// }
