package com.orderservice.server.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.*;

@Configuration
@EnableWebSecurity
public class OAuth2WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;

  @Bean
  public ResourceServerTokenServices tokenServices() {
    RemoteTokenServices tokenServices = new RemoteTokenServices();
    tokenServices.setClientId("orderService");
    tokenServices.setClientSecret("123456");
    tokenServices.setCheckTokenEndpointUrl("http://localhost:8090/oauth/check_token");
    tokenServices.setAccessTokenConverter(getAccessTokenConverter());
    return tokenServices;
  }

  private AccessTokenConverter getAccessTokenConverter() {
    DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
    DefaultUserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter();
    userAuthenticationConverter.setUserDetailsService(userDetailsService);
    accessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
    return accessTokenConverter;
  }

  // check user details
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
    authenticationManager.setTokenServices(tokenServices());
    return authenticationManager;
  }
}
