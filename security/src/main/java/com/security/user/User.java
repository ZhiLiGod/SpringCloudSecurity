package com.security.user;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name ="id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @NotBlank(message = "Username cannot be null")
  @Column(name = "username", unique = true, nullable = false)
  private String username;

  @NotBlank(message = "Password cannot be null")
  @Column(name = "password", nullable = false)
  private String password;

}
