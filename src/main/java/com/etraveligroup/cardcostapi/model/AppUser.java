package com.etraveligroup.cardcostapi.model;

import com.etraveligroup.cardcostapi.util.*;
import com.etraveligroup.cardcostapi.util.Role;
import java.util.Collection;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "app_user")
public class AppUser implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;
  private String password;

  @ElementCollection(fetch = FetchType.EAGER)
  private Set<Role> roles; // Assuming roles are stored as an enum or string

  // Private constructor for the builder
  private AppUser(Builder builder) {
    this.id = builder.id;
    this.username = builder.username;
    this.password = builder.password;
    this.roles = builder.roles;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.name())) // Convert Role to GrantedAuthority
        .toList();
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

  public Set<Role> getRoles() {
    return roles;
  }

  // Static Builder class
  public static class Builder {
    private Long id;
    private String username;
    private String password;
    private Set<Role> roles;

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

    public Builder roles(Set<Role> roles) {
      this.roles = roles;
      return this;
    }

    public AppUser build() {
      return new AppUser(this);
    }
  }
}
