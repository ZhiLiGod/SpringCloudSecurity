package com.security.filter;

import com.security.dto.UserInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Order(4)
public class ACLInterceptor extends HandlerInterceptorAdapter {

  private String[] permitUrls = new String[] {"/login/session"};

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    Boolean result = Boolean.TRUE;

    if (!ArrayUtils.contains(permitUrls, request.getRequestURI())) {
      UserInfo user = (UserInfo) request.getSession().getAttribute("user");
      if (user == null) {
        response.setContentType("text/plain");
        response.getWriter().write("need authentication");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        result = Boolean.FALSE;
      } else {
        String method = request.getMethod();

        if (!hasPermissions(method, user.getPermissions())) {
          response.setContentType("text/plain");
          response.getWriter().write("forbidden");
          response.setStatus(HttpStatus.FORBIDDEN.value());
          result = Boolean.FALSE;
        }
      }
    }

    return result;
  }

  private boolean hasPermissions(String requestMethod, String permission) {
    boolean result = Boolean.FALSE;

    if (StringUtils.equalsAnyIgnoreCase("get", requestMethod)) {
      result = StringUtils.contains(permission, "r");
    } else {
      result = StringUtils.contains(permission, "w");
    }

    return result;
  }

}
