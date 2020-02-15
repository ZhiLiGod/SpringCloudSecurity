package com.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class AuthorizationFilter extends ZuulFilter {

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 3;
  }

  @Override
  public boolean shouldFilter() {
    return Boolean.TRUE;
  }

  @Override
  public Object run() throws ZuulException {
    log.info("authorizatin start");

    RequestContext context = RequestContext.getCurrentContext();
    HttpServletRequest request = context.getRequest();

    if (isNeedAuth(request)) {
      TokenInfo tokenInfo = (TokenInfo) request.getAttribute("tokenInfo");
      
      if (tokenInfo != null && tokenInfo.isActive()) {
        if (!hasPermission(tokenInfo, request)) {
          log.info("audit log update fail 403");
          handleError(403, context);
        }

        context.addZuulRequestHeader("username", tokenInfo.getUser_name());

      } else {
        if (!StringUtils.startsWith(request.getRequestURI(), "/token")) {
          log.info("audit log update fail 401");
          handleError(401, context);
        }
      }
    }

    return null;
  }

  private boolean hasPermission(TokenInfo tokenInfo, HttpServletRequest request) {
    return Boolean.TRUE;
  }

  private void handleError(int status, RequestContext context) {
    context.getResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);
    context.setResponseStatusCode(status);
    context.setResponseBody("{\"message\":\"auth fail\"}");
    context.setSendZuulResponse(Boolean.FALSE);
  }

  private boolean isNeedAuth(HttpServletRequest request) {
    return Boolean.TRUE;
  }

}
