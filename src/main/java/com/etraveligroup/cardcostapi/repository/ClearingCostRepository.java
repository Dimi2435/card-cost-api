package com.etraveligroup.cardcostapi.repository;

import com.etraveligroup.cardcostapi.model.entity.ClearingCostEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Author: Dimitrios Milios
// Repository interface for accessing clearing cost data.
// This interface extends JpaRepository to provide CRUD operations.
@Repository
public interface ClearingCostRepository extends JpaRepository<ClearingCostEntity, String> {

  // Spring Data JPA automatically provides basic CRUD operations (save, findById, findAll, delete)
  // You can add custom queries here if needed, e.g.,
  Optional<ClearingCostEntity> findByCountryCode(String countryCode);
}
