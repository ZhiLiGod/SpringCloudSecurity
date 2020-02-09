package com.security.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserInfo {

  private Long id;
  private String name;

  @NotBlank(message = "Username cannot be null")
  private String username;

  @NotBlank(message = "Password cannot be null")
  private String password;

  private String permissions;

}
