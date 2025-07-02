package com.etraveligroup.cardcostapi.repository;

import com.etraveligroup.cardcostapi.model.entity.ClearingCostEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Author: Dimitrios Milios
// Repository interface for accessing clearing cost data.
// This interface extends JpaRepository to provide CRUD operations.

/**
 * Repository interface for accessing clearing cost data.
 * This interface extends JpaRepository to provide CRUD operations.
 */
@Repository
public interface ClearingCostRepository extends JpaRepository<ClearingCostEntity, String> {

  /**
   * Finds a clearing cost entity by its country code.
   *
   * @param countryCode The country code to search for.
   * @return An Optional containing the ClearingCostEntity if found, otherwise empty.
   */
  Optional<ClearingCostEntity> findByCountryCode(String countryCode);
}
