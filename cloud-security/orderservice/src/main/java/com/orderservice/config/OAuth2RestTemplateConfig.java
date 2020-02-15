package com.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

@Configuration
public class OAuth2RestTemplateConfig {

  @Bean
  public OAuth2RestTemplate getRestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
    return new OAuth2RestTemplate(resource, context);
  }

}
