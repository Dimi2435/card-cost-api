package com.etraveligroup.cardcostapi.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for rate limiting. Provides basic rate limiting functionality for API
 * endpoints.
 */
@Configuration
public class RateLimitingConfig implements WebMvcConfigurer {

  @Value("${app.rate-limiting.enabled:true}")
  private boolean rateLimitingEnabled;

  @Value("${app.rate-limiting.max-requests:7000}")
  private int maxRequests;

  @Value("${app.rate-limiting.window-duration:60000}")
  private long windowDurationMs;

  @Bean
  public RateLimitingInterceptor rateLimitingInterceptor() {
    return new RateLimitingInterceptor(maxRequests, windowDurationMs);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    if (rateLimitingEnabled) {
      registry
          .addInterceptor(rateLimitingInterceptor())
          .addPathPatterns("/api/**"); // Apply to all API endpoints
    }
  }

  /**
   * Simple in-memory rate limiting interceptor. For production, consider using Redis or a
   * distributed rate limiter.
   */
  public static class RateLimitingInterceptor implements HandlerInterceptor {
    private final int maxRequests;
    private final long windowDurationMs;
    private final ConcurrentHashMap<String, AtomicInteger> requestCounts =
        new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public RateLimitingInterceptor(int maxRequests, long windowDurationMs) {
      this.maxRequests = maxRequests;
      this.windowDurationMs = windowDurationMs;

      // Reset counts periodically
      scheduler.scheduleAtFixedRate(
          requestCounts::clear, windowDurationMs, windowDurationMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

      String clientId = getClientIdentifier(request);
      AtomicInteger count = requestCounts.computeIfAbsent(clientId, k -> new AtomicInteger(0));

      if (count.incrementAndGet() > maxRequests) {
        response.setStatus(429); // Too Many Requests
        response.getWriter().write("{\"error\":\"Rate limit exceeded. Please try again later.\"}");
        response.setContentType("application/json");
        return false;
      }

      return true;
    }

    private String getClientIdentifier(HttpServletRequest request) {
      // Simple client identification by IP
      // For production, consider using user ID, API key, or more sophisticated identification
      String xForwardedFor = request.getHeader("X-Forwarded-For");
      if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
        return xForwardedFor.split(",")[0].trim();
      }
      return request.getRemoteAddr();
    }
  }
}
