package com.etraveligroup.cardcostapi.repository;

import com.etraveligroup.cardcostapi.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
  // Custom query method to find a user by username
  AppUser findByUsername(String username);
}
