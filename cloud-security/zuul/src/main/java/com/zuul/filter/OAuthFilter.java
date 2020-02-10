package com.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class OAuthFilter extends ZuulFilter {

  private RestTemplate restTemplate = new RestTemplate();

  @Override
  public String filterType() {
    // "pre", "post", "error", "route"
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 1;
  }

  @Override
  public boolean shouldFilter() {
    return Boolean.TRUE;
  }

  @Override
  public Object run() throws ZuulException {
    log.info("oauth start");

    RequestContext requestContext = RequestContext.getCurrentContext();
    HttpServletRequest request = requestContext.getRequest();

    if (StringUtils.startsWith(request.getRequestURI(), "/token")) {
      return null;
    }

    String authHeader = request.getHeader("Authorization");

    if (StringUtils.isBlank(authHeader)) {
      return null;
    }

    if (!StringUtils.startsWithIgnoreCase(authHeader, "bearer ")) {
      return null;
    }

    try {
      TokenInfo info = getTokenInfo(authHeader);
      request.setAttribute("tokenInfo", info);
    } catch (Exception e) {
      log.error("get token info failed. ", e);
    }

    return null;
  }

  private TokenInfo getTokenInfo(String authHeader) {
    log.info("auth header {}", authHeader);
    String token = StringUtils.substringAfter(authHeader, "bearer ");
    String oauthServiceUrl = "http://localhost:8090/oauth/check_token";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.setBasicAuth("gateway", "123456");

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("token", token);

    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
    ResponseEntity<TokenInfo> response = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);

    log.info("token info: {}", response.getBody().toString());
    return response.getBody();
  }

}
