package com.admin.filter;

import com.admin.dto.TokenInfo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class SessionTokenFilter extends ZuulFilter {
  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 0;
  }

  @Override
  public boolean shouldFilter() {
    return Boolean.TRUE;
  }

  @Override
  public Object run() throws ZuulException {
    RequestContext requestContext = RequestContext.getCurrentContext();
    HttpServletRequest request = requestContext.getRequest();
    TokenInfo token = (TokenInfo) request.getSession().getAttribute("token");

    if (token != null) {
      requestContext.addZuulRequestHeader("Authorization", "bearea " + token.getAccess_token());
    }

    return null;
  }
}
