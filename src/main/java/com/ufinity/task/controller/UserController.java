package com.ufinity.task.controller;

import com.ufinity.task.model.UserModel;
import com.ufinity.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  };

  @PostMapping("user/signup")
  public Map<String, String> signup(@RequestBody UserModel userModel) {
    Map<String, String> resultMap = new HashMap<>();
    String signUpCode = userService.signup(userModel.getUsername(), userModel.getPassword(), userModel.getEmail());

    resultMap.put("code", signUpCode);
    return resultMap;
  }

}
