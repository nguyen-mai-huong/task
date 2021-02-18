package com.ufinity.task.controller;

import com.ufinity.task.model.UserModel;
import com.ufinity.task.model.UserQueryModel;
import com.ufinity.task.service.UserQueryService;
import com.ufinity.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ufinity.task.utils.Constants.OK;

@RestController
public class UserController {
  private UserService userService;
  private UserQueryService userQueryService;

  @Autowired
  public UserController(UserService userService, UserQueryService userQueryService) {
    this.userService = userService;
    this.userQueryService = userQueryService;
  };

  @PostMapping("user/signup")
  public Map<String, String> signup(@RequestBody UserModel userModel) {
    Map<String, String> resultMap = new HashMap<>();
    String signUpCode = userService.signup(userModel.getUsername(), userModel.getPassword(), userModel.getEmail());

    resultMap.put("code", signUpCode);
    return resultMap;
  }

  @GetMapping("user/list")
  @PreAuthorize("hasAuthority('SYS_ADMIN')")
  public Map<String, Object> getUsers(@RequestParam("recordsPerPage") int recordsPerPage,
                                      @RequestParam(value = "leftCursor", required = false) Long leftUserId,
                                      @RequestParam(value = "rightCursor", required = false) Long rightUserId, HttpServletResponse response) {
    Map<String, Object> resultMap = new HashMap<>();
    List<UserQueryModel> userResultList = new ArrayList<>();
    Long userCount = null;

    if (leftUserId == null && rightUserId == null) {
      userResultList = userQueryService.getUsers(recordsPerPage);
      userCount = userQueryService.countUsers();
    } else if (leftUserId != null) {
      userResultList = userQueryService.getPreviousUsers(recordsPerPage, leftUserId);
    } else if (rightUserId != null) {
      userResultList = userQueryService.getNextUsers(recordsPerPage, rightUserId);
    }

    resultMap.put("code", OK);
    resultMap.put("userList", userResultList);
    if (userCount != null) {
      resultMap.put("userCount", userCount);
    }

    response.addHeader("Access-Control-Allow-Credentials", "true");

    return resultMap;
  }



}
