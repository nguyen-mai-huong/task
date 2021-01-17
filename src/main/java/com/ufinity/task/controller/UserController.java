package com.ufinity.task.controller;

import com.ufinity.task.model.User;
import com.ufinity.task.model.UserModel;
import com.ufinity.task.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ufinity.task.utils.Constants.ERROR;
import static com.ufinity.task.utils.Constants.OK;

@CrossOrigin(origins = "*", allowedHeaders = "*")
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

  @PostMapping("user/login")
  public Map<String, String> login(@RequestParam String username, @RequestParam String password) {
    Map<String, String> resultMap = new HashMap<>();
    String loginCode = userService.login(username, password);

    if (loginCode.equals(OK)) {
      resultMap.put("code", OK);
    } else {
      resultMap.put("code", ERROR);
      resultMap.put("errors", loginCode);
    }

    return resultMap;
  }

  @PostMapping("user/signup")
  public Map<String, String> signup(@RequestBody UserModel userModel) {
    Map<String, String> resultMap = new HashMap<>();
    String signUpCode = userService.signup(userModel.getUsername(), userModel.getPassword(), userModel.getEmail());

    resultMap.put("code", signUpCode);
    return resultMap;
  }





}
