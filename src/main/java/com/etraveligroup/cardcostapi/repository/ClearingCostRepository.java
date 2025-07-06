package com.etraveligroup.cardcostapi.repository;

import com.etraveligroup.cardcostapi.model.ClearingCost;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for clearing cost data. This interface extends JpaRepository to provide CRUD
 * operations and custom query methods for clearing costs.
 *
 * <p>Author: Dimitrios Milios
 */
@Repository
public interface ClearingCostRepository extends JpaRepository<ClearingCost, Long> {
  Optional<ClearingCost> findByCountryCode(String countryCode);
  // List<ClearingCostEntity> findAll();
  // ClearingCostEntity save(ClearingCostEntity cost);
  // void delete(ClearingCostEntity cost);
}
