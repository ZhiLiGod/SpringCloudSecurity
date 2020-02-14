package com.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/logout")
public class LogoutController {

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public void logout(HttpServletRequest request) {
    request.getSession().invalidate();
  }

}
