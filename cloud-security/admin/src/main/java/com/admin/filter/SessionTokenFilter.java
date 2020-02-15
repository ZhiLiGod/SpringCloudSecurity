package com.admin.filter;

import com.admin.dto.TokenInfo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class SessionTokenFilter extends ZuulFilter {

  private RestTemplate restTemplate = new RestTemplate();

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
      String tokenValue = token.getAccess_token();
      // check if token expired, if so we refresh token
      if (token.isExpired()) {
        String oauthServiceUrl = "http://gateway-server:9000/token/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("admin", "123456");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", token.getRefresh_token());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<TokenInfo> response = null;

        // refresh token failed
        try {
          response = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
          request.getSession().setAttribute("token", response.getBody().init());
          tokenValue = response.getBody().getAccess_token();
        } catch (RestClientException e) {
          log.error("Refresh token failed.");
          requestContext.setSendZuulResponse(Boolean.FALSE);
          requestContext.setResponseStatusCode(500);
          requestContext.setResponseBody("{\"message\":\"refresh fail\"}");
          requestContext.getResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);
        }

      }

      requestContext.addZuulRequestHeader("Authorization", "bearer " + tokenValue);
    }

    return null;
  }
}
