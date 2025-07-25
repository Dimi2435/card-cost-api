package com.etraveligroup.cardcostapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Represents a user in the application. This class implements UserDetails to provide user
 * information for Spring Security.
 *
 * <p>Author: Dimitrios Milios
 */
@Entity
@Table(name = "app_user")
public class AppUser implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;
  private String password;

  private String roles; // Store roles as a comma-separated string

  // Default constructor for JPA
  protected AppUser() {
    // Default constructor for JPA
  }

  // Private constructor for the builder
  private AppUser(Builder builder) {
    this.id = builder.id;
    this.username = builder.username;
    this.password = builder.password;
    this.roles = builder.roles;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // Parse roles from comma-separated string
    if (roles == null || roles.trim().isEmpty()) {
      return List.of();
    }
    return Arrays.stream(roles.split(","))
        .map(String::trim)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

  @Override
  public String getUsername() {
    return username; // Return the username field
  }

  @Override
  public boolean isAccountNonExpired() {
    // Implementation needed
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    // Implementation needed
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // Implementation needed
    return true;
  }

  @Override
  public boolean isEnabled() {
    // Implementation needed
    return true;
  }

  // Getters and Setters for id, username, password, and roles
  public Long getId() {
    return id;
  }

  public String getPassword() {
    return password;
  }

  public String getRoles() {
    return roles;
  }

  // Static Builder class
  public static class Builder {
    private Long id;
    private String username;
    private String password;
    private String roles;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder roles(String roles) {
      this.roles = roles;
      return this;
    }

    public AppUser build() {
      return new AppUser(this);
    }
  }
}
