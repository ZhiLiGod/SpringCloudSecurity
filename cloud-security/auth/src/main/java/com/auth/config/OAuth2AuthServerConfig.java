package com.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import javax.sql.DataSource;

@EnableJdbcHttpSession
@EnableAuthorizationServer
@Configuration
public class OAuth2AuthServerConfig extends AuthorizationServerConfigurerAdapter {

  @Autowired
  private AuthenticationManager authenticationManager;

  // this has to under authenticationManager
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private DataSource dataSource;

  @Autowired
  private UserDetailsService userDetailsService;

  @Bean
  public TokenStore tokenStore() {
    // return new JdbcTokenStore(dataSource);
    return new JwtTokenStore(jwtTokenEnhancer());
  }

  private JwtAccessTokenConverter jwtTokenEnhancer() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setSigningKey("123456");
    return converter;
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    // @formatter:off
//    clients.inMemory()
//        .withClient("orderApp")
//        .secret(passwordEncoder.encode("123456"))
//        .scopes("read", "write")
//        .accessTokenValiditySeconds(3600)
//        .resourceIds("order-server")
//        .authorizedGrantTypes("password")
//        .and()
//        .withClient("orderService")
//        .secret(passwordEncoder.encode("123456"))
//        .scopes("read")
//        .accessTokenValiditySeconds(3600)
//        .resourceIds("order-server")
//        .authorizedGrantTypes("password");
    // @formatter:on

    clients.jdbc(dataSource);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    // @formatter:off
    endpoints
        .userDetailsService(userDetailsService)// for refresh token
        .tokenStore(tokenStore())
        .tokenEnhancer(jwtTokenEnhancer())
        .authenticationManager(authenticationManager);
    // @formatter:on
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security.tokenKeyAccess("isAuthenticated()").checkTokenAccess("isAuthenticated()");
  }

}
