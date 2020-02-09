package com.security.filter;

import com.lambdaworks.crypto.SCryptUtil;
import com.security.dto.UserInfo;
import com.security.user.User;
import com.security.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Component
@Order(2)
public class BasicAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private UserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    String authHeader = httpServletRequest.getHeader("Authorization");

    if (StringUtils.isNotBlank(authHeader)) {
      String token64 = StringUtils.substringAfter(authHeader, "Basic ");
      String token = new String(Base64Utils.decodeFromString(token64));
      String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(token, ":");

      String username = items[0];
      String password = items[1];
      User user = userRepository.findByUsername(username);

      if (user != null && SCryptUtil.check(password, user.getPassword())) {
        log.info("LOGIN");
        httpServletRequest.getSession().setAttribute("user", userInfoConverter(user));
        httpServletRequest.getSession().setAttribute("temp", "yes");
      }
    }

    try {
      filterChain.doFilter(httpServletRequest, httpServletResponse);
    } finally {

      HttpSession session = httpServletRequest.getSession();
      if (session.getAttribute("temp") != null) {
        session.invalidate();
      }
    }
  }

  private UserInfo userInfoConverter(User user) {
    UserInfo userInfo = new UserInfo();
    BeanUtils.copyProperties(user, userInfo);
    return userInfo;
  }

}
