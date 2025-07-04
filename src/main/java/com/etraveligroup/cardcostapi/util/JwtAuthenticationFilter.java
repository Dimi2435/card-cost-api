package com.etraveligroup.cardcostapi.config; // Assuming this is correct package

import com.etraveligroup.cardcostapi.util.JwtUtil; // Assuming this class exists and works
import jakarta.servlet.FilterChain; // <-- Changed from javax.servlet
import jakarta.servlet.ServletException; // <-- Changed from javax.servlet
import jakarta.servlet.http.HttpServletRequest; // <-- Changed from javax.servlet.http
import jakarta.servlet.http.HttpServletResponse; // <-- Changed from javax.servlet.http
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // <-- Need this
import org.springframework.web.filter.OncePerRequestFilter; // <-- Use this base class

public class JwtAuthenticationFilter
    extends OncePerRequestFilter { // <-- Extend OncePerRequestFilter

  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService; // <-- Add this dependency

  @Autowired
  public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    final String authorizationHeader = request.getHeader("Authorization");

    String username = null;
    String jwt = null;

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
      username = jwtUtil.extractUsername(jwt);
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      if (jwtUtil.validateToken(jwt, username)) {
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    chain.doFilter(request, response);
  }
}
