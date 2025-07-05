package com.etraveligroup.cardcostapi.config;

// IMPORTANT: Use jakarta imports for servlet API if you have them in your JwtAuthenticationFilter
// import jakarta.servlet.http.HttpServletRequest; etc.

import com.etraveligroup.cardcostapi.model.AppUser;
import com.etraveligroup.cardcostapi.util.*;
import com.etraveligroup.cardcostapi.util.AppConstants;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${app.default.username}")
  private String defaultUsername;

  @Value("${app.default.password}")
  private String defaultPassword;

  @Value("${app.admin.username}")
  private String adminUsername;

  @Value("${app.admin.password}")
  private String adminPassword;

  public SecurityConfig(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  // --- Authentication Components (as provided in previous answer) ---
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    System.out.println("user: USER USERNAME " + defaultUsername);
    System.out.println("admin: ADMIN USERNAME " + adminUsername);

    System.out.println("Raw password: USER PASSWORD " + defaultPassword);
    System.out.println("Raw password: ADMIN PASSWORD " + adminPassword);

    System.out.println(
        "encoded password: USER PASSWORD " + passwordEncoder().encode(defaultPassword));
    System.out.println(
        "encoded password: ADMIN PASSWORD " + passwordEncoder().encode(adminPassword));

    UserDetails user =
        new AppUser.Builder()
            .username(defaultUsername)
            .password(passwordEncoder().encode(defaultPassword))
            .roles(Set.of(Role.ROLE_USER)) // Assuming Role is an enum
            .build();

    UserDetails admin =
        new AppUser.Builder()
            .username(adminUsername)
            .password(passwordEncoder().encode(adminPassword))
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
        .headers(
            headers ->
                headers.frameOptions(
                    frameOptions -> frameOptions.disable())) // Allow H2 console to work in frames
        .authorizeHttpRequests(
            authorize ->
                authorize
                    // Allow H2 console access (development only)
                    .requestMatchers("/h2-console/**")
                    .permitAll()
                    // Allow Swagger UI and API docs
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html")
                    .permitAll()
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
