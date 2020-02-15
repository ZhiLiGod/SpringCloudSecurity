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
public class MeFilter extends ZuulFilter {
  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 4;
  }

  @Override
  public boolean shouldFilter() {
    RequestContext requestContext = RequestContext.getCurrentContext();
    HttpServletRequest request = requestContext.getRequest();
    return StringUtils.equals(request.getRequestURI(), "/user/me");
  }

  @Override
  public Object run() throws ZuulException {
    RequestContext requestContext = RequestContext.getCurrentContext();
    HttpServletRequest request = requestContext.getRequest();
    String user = requestContext.getZuulRequestHeaders().get("username");

    if (StringUtils.isNotBlank(user)) {
      requestContext.setResponseBody("{\"username\":\"" + user + "\"}");
    }
    requestContext.setSendZuulResponse(Boolean.FALSE);
    requestContext.setResponseStatusCode(200);
    requestContext.getResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);
    return null;
  }
}
