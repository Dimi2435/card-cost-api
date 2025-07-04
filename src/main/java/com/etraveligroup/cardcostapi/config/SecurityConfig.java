package com.etraveligroup.cardcostapi.config;

// IMPORTANT: Use jakarta imports for servlet API if you have them in your JwtAuthenticationFilter
// import jakarta.servlet.http.HttpServletRequest; etc.

import com.etraveligroup.cardcostapi.model.AppUser;
import com.etraveligroup.cardcostapi.util.*;
import com.etraveligroup.cardcostapi.util.AppConstants;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  private final JwtUtil jwtUtil;

  // You also need AppConstants as a component if you're injecting its values
  private final AppConstants appConstants;

  public SecurityConfig(JwtUtil jwtUtil, AppConstants appConstants) {
    this.jwtUtil = jwtUtil;
    this.appConstants = appConstants;
  }

  // --- Authentication Components (as provided in previous answer) ---
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails user =
        new AppUser.Builder()
            .username(AppConstants.DEFAULT_USERNAME)
            .password(passwordEncoder().encode(AppConstants.DEFAULT_PASSWORD))
            .roles(Set.of(Role.ROLE_USER)) // Assuming Role is an enum
            .build();

    UserDetails admin =
        new AppUser.Builder()
            .username(AppConstants.ADMIN_USERNAME)
            .password(passwordEncoder().encode(AppConstants.ADMIN_PASSWORD))
            .roles(Set.of(Role.ROLE_ADMIN)) // Assuming Role is an enum
            .build();

    return new InMemoryUserDetailsManager(user, admin);
  }

  @Bean
  public AuthenticationManager authenticationManager(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(authenticationProvider);
  }

  // --- Your JWT Authentication Filter as a Bean ---
  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(jwtUtil, userDetailsService());
  }

  // --- Security Filter Chain (Replaces WebSecurityConfigurerAdapter) ---
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable()) // Explicitly disable CSRF
        .authorizeHttpRequests(
            authorize ->
                authorize
                    // Allow login/auth endpoint if you have one
                    .requestMatchers("/authenticate")
                    .permitAll()
                    // Construct the path dynamically from AppConstants
                    .requestMatchers(
                        AppConstants.API_BASE_PATH
                            + "/v"
                            + AppConstants.DEFAULT_API_VERSION
                            + AppConstants.PAYMENT_CARDS_COST_ENDPOINT
                            + "**")
                    .authenticated() // Protect specific endpoints
                    .anyRequest()
                    .authenticated() // All other requests must be authenticated by default
            )
        .sessionManagement(
            session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS) // Use stateless sessions for JWT
            )
        // Add your custom JWT filter before the standard UsernamePasswordAuthenticationFilter
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
