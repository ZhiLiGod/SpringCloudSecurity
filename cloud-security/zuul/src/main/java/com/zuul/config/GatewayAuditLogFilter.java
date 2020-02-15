package com.zuul.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class GatewayAuditLogFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String user = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Add log for {}", user);
    filterChain.doFilter(request, response);

    if (StringUtils.isBlank((String) request.getAttribute("logUpdated"))) {
      log.info("Update log to success");
    }
  }

}
