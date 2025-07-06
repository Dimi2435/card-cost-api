package com.etraveligroup.cardcostapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for WebClient beans. Provides configured WebClient instances for external API
 * calls.
 *
 * <p>Author: Dimitrios Milios
 */
@Configuration
public class WebClientConfig {

  @Value("${app.external.binlist.url}")
  private String binlistBaseUrl;

  /**
   * Provides a WebClient.Builder bean for creating WebClient instances.
   *
   * @return a WebClient.Builder instance.
   */
  @Bean
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder()
        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024));
  }

  /**
   * Provides a WebClient bean configured with the base URL for the binlist API.
   *
   * @param webClientBuilder the WebClient.Builder for creating the WebClient.
   * @return a WebClient instance configured for the binlist API.
   */
  @Bean
  public WebClient binlistWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder.baseUrl(binlistBaseUrl).build();
  }
}
