package com.security.user;

import com.security.dto.UserInfo;
import com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private UserService userService;

  @PostMapping
  public UserInfo create(@RequestBody @Validated UserInfo user) {
    return userService.create(user);
  }

  @PutMapping
  public UserInfo update(@RequestBody UserInfo user) {
    return userService.update(user);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    userService.delete(id);
  }

  @GetMapping("/{id}/info")
  public UserInfo get(@PathVariable Long id, HttpServletRequest request) {
    User user = (User) request.getAttribute("user");

    if (user == null || !user.getId().equals(id)) {
      throw new RuntimeException("Authentication Failed");
    }

    return userService.get(id);
  }

  @GetMapping("/{name}")
  public List<UserInfo> getUsers(@PathVariable String name) {
    // injection issue
    String sql = "SELECT id, name FROM user WHERE name = '" + name +"'";
    List data = jdbcTemplate.queryForList(sql);

    // security way
    return userService.getUsers(name);
  }

}
