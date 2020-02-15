package com.zuul.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@Configuration
@EnableResourceServer
public class GatewaySecurityConfig extends ResourceServerConfigurerAdapter {

  @Autowired
  private GatewayWebSecurityExpressionHandler gatewayWebSecurityExpressionHandler;

  @Autowired
  private GatewayAccessDeniedHandler gatewayAccessDeniedHandler;

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    // @formatter:off
    resources
        .accessDeniedHandler(gatewayAccessDeniedHandler)
        .expressionHandler(gatewayWebSecurityExpressionHandler);
    // @formatter:on
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
        // 验证之前
        .addFilterBefore(new GatewayAuditLogFilter(), ExceptionTranslationFilter.class)
        .authorizeRequests()
        .antMatchers("/token/**").permitAll()
        .anyRequest().access("#permissionService.hasPermission(request, authentication)");
    // @formatter:on
  }
}
