package com.security.service;

import com.security.dto.UserInfo;

import java.util.List;

public interface UserService {

  UserInfo create(UserInfo userInfo);

  UserInfo update(UserInfo userInfo);

  void delete(Long id);

  UserInfo get(Long id);

  List<UserInfo> getUsers(String name);

  UserInfo login(UserInfo user);

}
