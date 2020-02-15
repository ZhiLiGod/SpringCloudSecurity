package com.zuul.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

  @Override
  public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
    log.info("URI: {}", request.getRequestURI());
    log.info("Authentication: {}", ReflectionToStringBuilder.toString(authentication));
    // this part should check db and see authorizes

    if (authentication instanceof AnonymousAuthenticationToken) {// no token
      throw new AccessTokenRequiredException(null);
    }
    return true;
  }

}
