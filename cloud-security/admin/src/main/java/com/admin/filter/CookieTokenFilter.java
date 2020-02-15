package com.admin.filter;

import com.admin.dto.TokenInfo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class CookieTokenFilter extends ZuulFilter {

  private RestTemplate restTemplate = new RestTemplate();

  @Override
  public String filterType() {
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
    RequestContext requestContext = RequestContext.getCurrentContext();
    HttpServletRequest request = requestContext.getRequest();
    HttpServletResponse httpServletResponse = requestContext.getResponse();

    String accessToken = getCookie("access_token");
    if (StringUtils.isNotBlank(accessToken)) {

      requestContext.addZuulRequestHeader("Authorization", "bearer " + accessToken);
    } else { // cookie expired
      String refreshToken = getCookie("refresh_token");
      if (StringUtils.isNotBlank(refreshToken)) {
        String oauthServiceUrl = "http://gateway-server:9000/token/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("admin", "123456");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<TokenInfo> response = null;

        // refresh token failed
        try {
          response = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
          requestContext.addZuulRequestHeader("Authorization", "bearer " + response.getBody().getAccess_token());

          Cookie cookie = new Cookie("access_token", response.getBody().getAccess_token());
          cookie.setMaxAge(response.getBody().getExpires_in().intValue() - 3);// 3 is a buffer
          cookie.setDomain("server.com");
          cookie.setPath("/");

          httpServletResponse.addCookie(cookie);

          Cookie refreshCookie = new Cookie("refresh_token", response.getBody().getRefresh_token());
          refreshCookie.setMaxAge(2592000);// 1 momnth
          refreshCookie.setDomain("server.com");
          refreshCookie.setPath("/");

          httpServletResponse.addCookie(refreshCookie);
        } catch (RestClientException e) {
          log.error("Refresh token failed.");
          requestContext.setSendZuulResponse(Boolean.FALSE);
          requestContext.setResponseStatusCode(500);
          requestContext.setResponseBody("{\"message\":\"refresh fail\"}");
          requestContext.getResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);
        }
      } else {
        log.error("no refresh token");
        requestContext.setSendZuulResponse(Boolean.FALSE);
        requestContext.setResponseStatusCode(500);
        requestContext.setResponseBody("{\"message\":\"refresh fail\"}");
        requestContext.getResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);
      }
    }
    return null;
  }

  private String getCookie(String key) {
    String result = null;
    RequestContext requestContext = RequestContext.getCurrentContext();
    HttpServletRequest request = requestContext.getRequest();

    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (StringUtils.equals(key, cookie.getName())) {
        result = cookie.getValue();
      }
    }

    return result;
  }
}
