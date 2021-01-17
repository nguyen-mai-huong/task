package com.ufinity.task.service;

import com.ufinity.task.model.User;

import java.util.List;

public interface IUserService {
  public List<User> listUsers();

  String login(String username, String password);

  String signup(String username, String password, String email);
}
