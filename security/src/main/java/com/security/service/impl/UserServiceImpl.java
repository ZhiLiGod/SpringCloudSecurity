package com.security.service.impl;

import com.lambdaworks.crypto.SCryptUtil;
import com.security.dto.UserInfo;
import com.security.service.UserService;
import com.security.user.User;
import com.security.user.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserInfo create(UserInfo userInfo) {
    User user = new User();
    BeanUtils.copyProperties(userInfo, user);
    user.setPassword(SCryptUtil.scrypt(user.getPassword(), 32768, 8, 1));
    user = userRepository.save(user);
    userInfo.setId(user.getId());

    return userInfo;
  }

  @Override
  public UserInfo update(UserInfo userInfo) {
    return null;
  }

  @Override
  public void delete(Long id) {

  }

  @Override
  public UserInfo get(Long id) {
    return userInfoConverter(userRepository.findById(id).orElse(new User()));
  }

  @Override
  public List<UserInfo> getUsers(String name) {
    return null;
  }

  @Override
  public UserInfo login(UserInfo user) {
    return user;
  }

  private UserInfo userInfoConverter(User user) {
    UserInfo userInfo = new UserInfo();
    BeanUtils.copyProperties(user, userInfo);
    return userInfo;
  }

}
