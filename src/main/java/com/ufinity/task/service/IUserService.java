package com.ufinity.task.service;

import com.ufinity.task.model.User;

import java.util.List;

public interface IUserService {
  public List<User> listUsers();

  public User findByUsername(String username);

  String signup(String username, String password, String email);

  List<String> getUserRoles(String username);
}
