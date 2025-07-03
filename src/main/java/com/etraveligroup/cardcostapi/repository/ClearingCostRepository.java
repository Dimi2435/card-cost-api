package com.etraveligroup.cardcostapi.repository;

import com.etraveligroup.cardcostapi.model.entity.ClearingCostEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// For BigDecimal

// Author: Dimitrios Milios
// Repository interface for clearing cost data.

/** Repository interface for clearing cost data. */
@Repository
public interface ClearingCostRepository extends JpaRepository<ClearingCostEntity, Long> {
  Optional<ClearingCostEntity> findByCountryCode(String countryCode);
  // List<ClearingCostEntity> findAll();
  // ClearingCostEntity save(ClearingCostEntity cost);
  // void delete(ClearingCostEntity cost);
}
