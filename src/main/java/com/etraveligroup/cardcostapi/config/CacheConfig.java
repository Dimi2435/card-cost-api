package com.etraveligroup.cardcostapi.config;

// Temporarily disable caching to fix application startup
// import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for caching. Provides caching functionality for improving performance.
 * Caching is temporarily disabled to fix startup issues.
 */
@Configuration
// @EnableCaching  // Temporarily disabled
public class CacheConfig {
  // Caching temporarily disabled to fix context loading
}
