package com.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private UserRepository userRepository;

  @PostMapping
  public User create(@RequestBody User user) {
    return user;
  }

  @PutMapping
  public User update(@RequestBody User user) {
    return user;
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {

  }

  @GetMapping("/{id}")
  public User get(@PathVariable Long id) {
    return new User();
  }

  @GetMapping("/{name}")
  public List<User> getUsers(@PathVariable String name) {
    // injection issue
    String sql = "SELECT id, name FROM user WHERE name = '" + name +"'";
    List data = jdbcTemplate.queryForList(sql);

    // security way
    List<User> users = userRepository.findByName(name);
    return data;
  }

}
