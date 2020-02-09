package com.orderservice.server.resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Override
  public void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http.authorizeRequests()
        .antMatchers(HttpMethod.POST)
        .access("#oauth2.hasScope('write')")
        .antMatchers(HttpMethod.GET)
        .access("#oauth2.hasScope('read')")
        .anyRequest().authenticated();
    // @formatter:on
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    // should match the one in AUTH
    resources.resourceId("order-server");
  }

}
