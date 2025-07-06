package com.etraveligroup.cardcostapi.config;

// IMPORTANT: Use jakarta imports for servlet API if you have them in your JwtAuthenticationFilter
// import jakarta.servlet.http.HttpServletRequest; etc.

import com.etraveligroup.cardcostapi.util.*;
import com.etraveligroup.cardcostapi.util.AppConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Security configuration class for the application. Configures security settings, including
 * authentication and authorization.
 *
 * <p>Author: Dimitrios Milios
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  private final JwtUtil jwtUtil;

  public SecurityConfig(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  /**
   * Provides a PasswordEncoder bean for encoding passwords.
   *
   * @return a BCryptPasswordEncoder instance.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Configures the AuthenticationManager bean.
   *
   * @param userDetailsService the UserDetailsService for authentication.
   * @param passwordEncoder the PasswordEncoder for encoding passwords.
   * @return an AuthenticationManager instance.
   */
  @Bean
  public AuthenticationManager authenticationManager(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(authenticationProvider);
  }

  /**
   * Provides a JwtAuthenticationFilter bean for JWT authentication.
   *
   * @param userDetailsService the UserDetailsService for authentication.
   * @return a JwtAuthenticationFilter instance.
   */
  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter(UserDetailsService userDetailsService) {
    return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
  }

  /**
   * Configures the security filter chain for the application.
   *
   * @param http the HttpSecurity object for configuring security.
   * @param corsConfigurationSource the CORS configuration source.
   * @param jwtAuthenticationFilter the JWT authentication filter.
   * @return a SecurityFilterChain instance.
   * @throws Exception if an error occurs during configuration.
   */
  @Bean
  SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      @Qualifier("corsConfigurationSource") CorsConfigurationSource corsConfigurationSource,
      JwtAuthenticationFilter jwtAuthenticationFilter)
      throws Exception {
    http.csrf(csrf -> csrf.disable()) // Explicitly disable CSRF
        .cors(cors -> cors.configurationSource(corsConfigurationSource)) // Enable CORS
        .headers(
            headers -> headers.frameOptions().sameOrigin()) // Allow H2 console to work in frames
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
                    // Allow actuator endpoints for monitoring
                    .requestMatchers("/actuator/**")
                    .permitAll()
                    // Protect the payment cards cost endpoints
                    .requestMatchers(
                        AppConstants.API_BASE_PATH
                            + "/v"
                            + AppConstants.DEFAULT_API_VERSION
                            + AppConstants.PAYMENT_CARDS_COST_ENDPOINT
                            + "**")
                    .hasAnyRole("ADMIN", "USER") // Allow both admin and user roles
                    .anyRequest()
                    .authenticated() // All other requests must be authenticated by default
            )
        .sessionManagement(
            session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS) // Use stateless sessions for JWT
            )
        // Add your custom JWT filter before the standard UsernamePasswordAuthenticationFilter
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
