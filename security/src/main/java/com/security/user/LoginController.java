package com.security.user;

import com.security.dto.UserInfo;
import com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

  @Autowired
  private UserService userService;

  @PostMapping("/login/session")
  @ResponseStatus(HttpStatus.OK)
  public void login(@RequestBody UserInfo user, HttpServletRequest request) {
    UserInfo info = userService.login(user);

    // avoid session fixation attack
    HttpSession session = request.getSession(Boolean.FALSE);
    if (session != null) {
      session.invalidate();
    }

    request.getSession(Boolean.TRUE).setAttribute("user", user);
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  public void logout(HttpServletRequest request) {
    request.getSession().invalidate();
  }

}
