package com.etraveligroup.cardcostapi.service;

import com.etraveligroup.cardcostapi.model.AppUser;
import com.etraveligroup.cardcostapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {
  @Autowired private UserRepository userRepository; // Your JPA repository
  @Autowired private BCryptPasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AppUser user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }
    return user;
  }

  public void registerUser(String username, String rawPassword) {
    // Check if the username already exists
    if (userRepository.findByUsername(username) != null) {
      throw new RuntimeException("Username already exists");
    }

    // Create a new user using the builder
    AppUser newUser =
        new AppUser.Builder()
            .username(username)
            .password(passwordEncoder.encode(rawPassword)) // Encode the raw password
            .build();

    // Save the user to the repository
    userRepository.save(newUser);
  }
}
