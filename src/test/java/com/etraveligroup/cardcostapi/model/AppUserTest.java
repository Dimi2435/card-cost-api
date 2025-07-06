package com.etraveligroup.cardcostapi.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

class AppUserTest {

  @Test
  void testGetAuthorities_WithRoles() {
    AppUser user =
        new AppUser.Builder()
            .username("testUser")
            .password("password")
            .roles("ROLE_USER, ROLE_ADMIN")
            .build();

    Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
    assertEquals(2, authorities.size(), "User should have 2 authorities");
    assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
  }

  @Test
  void testGetAuthorities_NoRoles() {
    AppUser user =
        new AppUser.Builder().username("testUser").password("password").roles("").build();

    Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
    assertTrue(authorities.isEmpty(), "User should have no authorities");
  }

  @Test
  void testGetUsername() {
    AppUser user =
        new AppUser.Builder().username("testUser").password("password").roles("ROLE_USER").build();

    assertEquals("testUser", user.getUsername(), "Username should match");
  }

  @Test
  void testIsAccountNonExpired() {
    AppUser user =
        new AppUser.Builder().username("testUser").password("password").roles("ROLE_USER").build();

    assertTrue(user.isAccountNonExpired(), "Account should not be expired");
  }

  @Test
  void testIsAccountNonLocked() {
    AppUser user =
        new AppUser.Builder().username("testUser").password("password").roles("ROLE_USER").build();

    assertTrue(user.isAccountNonLocked(), "Account should not be locked");
  }

  @Test
  void testIsCredentialsNonExpired() {
    AppUser user =
        new AppUser.Builder().username("testUser").password("password").roles("ROLE_USER").build();

    assertTrue(user.isCredentialsNonExpired(), "Credentials should not be expired");
  }

  @Test
  void testIsEnabled() {
    AppUser user =
        new AppUser.Builder().username("testUser").password("password").roles("ROLE_USER").build();

    assertTrue(user.isEnabled(), "User should be enabled");
  }
}
