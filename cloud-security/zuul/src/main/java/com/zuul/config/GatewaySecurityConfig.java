package com.zuul.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class GatewaySecurityConfig extends ResourceServerConfigurerAdapter {

  @Autowired
  private GatewayWebSecurityExpressionHandler gatewayWebSecurityExpressionHandler;

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources.expressionHandler(gatewayWebSecurityExpressionHandler);
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http.authorizeRequests()
        .antMatchers("/token/**").permitAll()
        .anyRequest().access("#permissionService.hasPermission(request, authentication)");
    // @formatter:on
  }
}
