package com.etraveligroup.cardcostapi.service;

import com.etraveligroup.cardcostapi.model.AppUser;
import com.etraveligroup.cardcostapi.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service for loading user-specific data. This class implements UserDetailsService to retrieve user
 * information from the database.
 *
 * <p>Author: Dimitrios Milios
 */
@Service
public class AppUserDetailsService implements UserDetailsService {
  @Autowired private AppUserRepository appUserRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AppUser user = appUserRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }
    return user;
  }
}
