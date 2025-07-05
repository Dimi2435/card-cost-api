package com.etraveligroup.cardcostapi.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;

  @Autowired
  public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    try {
      // Add security headers
      addSecurityHeaders(response, request);

      final String authorizationHeader = request.getHeader("Authorization");
      final String requestURI = request.getRequestURI();

      // Skip JWT validation for public endpoints
      if (isPublicEndpoint(requestURI)) {
        chain.doFilter(request, response);
        return;
      }

      String username = null;
      String jwt = null;

      // Extract JWT token
      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        jwt = authorizationHeader.substring(7);
        try {
          username = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
          logger.warn("Invalid JWT token format: {}", e.getMessage());
          sendUnauthorizedResponse(response, "Invalid JWT token format");
          return;
        }
      } else if (requiresAuthentication(requestURI)) {
        // Missing Authorization header for protected endpoint
        sendUnauthorizedResponse(response, "Missing or invalid Authorization header");
        return;
      }

      // Validate and set authentication
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        try {
          UserDetails userDetails = userDetailsService.loadUserByUsername(username);
          if (jwtUtil.validateToken(jwt, username)) {
            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            logger.debug("Successfully authenticated user: {}", username);
          } else {
            logger.warn("JWT token validation failed for user: {}", username);
            sendUnauthorizedResponse(response, "Invalid or expired JWT token");
            return;
          }
        } catch (Exception e) {
          logger.error("Authentication failed for user {}: {}", username, e.getMessage());
          SecurityContextHolder.clearContext();
          sendUnauthorizedResponse(response, "Authentication failed");
          return;
        }
      }

      chain.doFilter(request, response);
    } catch (Exception e) {
      logger.error("Error in JWT authentication filter: {}", e.getMessage());
      sendInternalErrorResponse(response, "Authentication processing error");
    }
  }

  private void addSecurityHeaders(HttpServletResponse response, HttpServletRequest request) {
    // Security headers for both HTTP and HTTPS
    response.setHeader("X-Content-Type-Options", "nosniff");
    response.setHeader("X-Frame-Options", "DENY");
    response.setHeader("X-XSS-Protection", "1; mode=block");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Expires", "0");

    // If HTTPS, add HSTS header
    if (request.isSecure()) {
      response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
    }
  }

  private boolean isPublicEndpoint(String requestURI) {
    return requestURI.startsWith("/h2-console")
        || requestURI.startsWith("/swagger-ui")
        || requestURI.startsWith("/v3/api-docs")
        || requestURI.equals("/swagger-ui.html")
        || requestURI.equals("/authenticate")
        || requestURI.startsWith("/actuator")
        || requestURI.equals("/")
        || requestURI.equals("/favicon.ico");
  }

  private boolean requiresAuthentication(String requestURI) {
    return requestURI.startsWith("/api/");
  }

  private void sendUnauthorizedResponse(HttpServletResponse response, String message)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response
        .getWriter()
        .write(
            String.format(
                "{\"error\":\"Unauthorized\",\"message\":\"%s\",\"status\":401}", message));
  }

  private void sendInternalErrorResponse(HttpServletResponse response, String message)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    response.setContentType("application/json");
    response
        .getWriter()
        .write(
            String.format(
                "{\"error\":\"Internal Server Error\",\"message\":\"%s\",\"status\":500}",
                message));
  }
}
