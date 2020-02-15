package com.zuul.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class GatewayAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    if (authException instanceof AccessTokenRequiredException) {// no token
      log.info("2. update log to 401");
    } else {// wrong token
      log.info("2. add log to 401");
    }

    request.setAttribute("logUpdated", "yes");
    super.commence(request, response, authException);
  }
}
