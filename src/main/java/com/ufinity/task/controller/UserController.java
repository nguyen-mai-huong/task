package com.ufinity.task.controller;

import com.ufinity.task.model.User;
import com.ufinity.task.service.IUserService;
import com.ufinity.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  };

  @GetMapping("users/list")
  public List<User> listUsers() {
    return userService.listUsers();
  }





}
