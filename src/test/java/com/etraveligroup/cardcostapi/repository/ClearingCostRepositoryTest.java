// package com.etraveligroup.cardcostapi.repository;

// import static org.junit.jupiter.api.Assertions.*;

// import com.etraveligroup.cardcostapi.model.ClearingCost;
// import java.math.BigDecimal;
// import java.util.Optional;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
// import org.springframework.test.context.ActiveProfiles;

/**
 * Unit tests for ClearingCostRepository class. This class tests the repository methods for managing
 * clearing cost entities.
 *
 * <p>Author: Dimitrios Milios
 */
// @DataJpaTest
// @ActiveProfiles("test")
// class ClearingCostRepositoryTest {

//   @Autowired private TestEntityManager entityManager;

//   @Autowired private ClearingCostRepository clearingCostRepository;

//   private ClearingCost clearingCost;

//   @BeforeEach
//   void setUp() {
//     clearingCost = new ClearingCost();
//     clearingCost.setCountryCode("US");
//     clearingCost.setCost(BigDecimal.valueOf(5.00));
//   }

//   @Test
//   void findByCountryCode_ExistingCountryCode_ReturnsClearingCost() {
//     // Arrange
//     entityManager.persistAndFlush(clearingCost);

//     // Act
//     Optional<ClearingCost> result = clearingCostRepository.findByCountryCode("US");

//     // Assert
//     assertTrue(result.isPresent());
//     assertEquals("US", result.get().getCountryCode());
//     assertEquals(BigDecimal.valueOf(5.00), result.get().getCost());
//   }

//   @Test
//   void findByCountryCode_NonExistingCountryCode_ReturnsEmpty() {
//     // Act
//     Optional<ClearingCost> result = clearingCostRepository.findByCountryCode("XX");

//     // Assert
//     assertFalse(result.isPresent());
//   }

//   @Test
//   void save_ValidClearingCost_SavesSuccessfully() {
//     // Act
//     ClearingCost saved = clearingCostRepository.save(clearingCost);

//     // Assert
//     assertNotNull(saved.getId());
//     assertEquals("US", saved.getCountryCode());
//     assertEquals(BigDecimal.valueOf(5.00), saved.getCost());
//   }

//   @Test
//   void findById_ExistingId_ReturnsClearingCost() {
//     // Arrange
//     ClearingCost saved = entityManager.persistAndFlush(clearingCost);

//     // Act
//     Optional<ClearingCost> result = clearingCostRepository.findById(saved.getId());

//     // Assert
//     assertTrue(result.isPresent());
//     assertEquals(saved.getId(), result.get().getId());
//     assertEquals("US", result.get().getCountryCode());
//   }

//   @Test
//   void delete_ExistingClearingCost_DeletesSuccessfully() {
//     // Arrange
//     ClearingCost saved = entityManager.persistAndFlush(clearingCost);
//     Long id = saved.getId();

//     // Act
//     clearingCostRepository.delete(saved);
//     entityManager.flush();

//     // Assert
//     Optional<ClearingCost> result = clearingCostRepository.findById(id);
//     assertFalse(result.isPresent());
//   }
// }
