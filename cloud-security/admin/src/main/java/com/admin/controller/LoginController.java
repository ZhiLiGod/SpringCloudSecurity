package com.admin.controller;

import com.admin.dto.Credential;
import com.admin.dto.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

  private RestTemplate restTemplate = new RestTemplate();

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public String login(@RequestBody Credential credential, HttpServletRequest request) {

    String oauthServiceUrl = "http://localhost:9000/token/oauth/token";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.setBasicAuth("admin", "123456");

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("username", credential.getUsername());
    params.add("password", credential.getPassword());
    params.add("grant_type", "password");
    params.add("scope", "read write");

    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
    ResponseEntity<TokenInfo> response = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);

    request.getSession().setAttribute("token", response.getBody());

    log.info(response.getBody().getAccess_token());

    return "User Logged In";
  }

  @GetMapping("/oauth/callback")
  public void callback(@RequestParam String code, String state, HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
    log.info("state: {}", state);

    String oauthServiceUrl = "http://localhost:9000/token/oauth/token";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.setBasicAuth("admin", "123456");

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("code", code);
    params.add("grant_type", "authorization_code");
    params.add("redirect_uri", "http://admin-server:8082/oauth/callback");

    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
    ResponseEntity<TokenInfo> response = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);

    request.getSession().setAttribute("token", response.getBody().init());

    log.info(response.getBody().getAccess_token());

    httpServletResponse.sendRedirect("/");
  }

  @GetMapping("/me")
  public TokenInfo me(HttpServletRequest request) {
    return (TokenInfo) request.getSession().getAttribute("/token");
  }

}
