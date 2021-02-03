package com.ufinity.task.controller;

import com.ufinity.task.service.OAuth2LoginService;
import com.ufinity.task.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.ufinity.task.utils.Constants.OK;

@RestController
public class OAuth2LoginController {

  @Autowired
  private OAuth2LoginService oAuth2LoginService;

  @GetMapping("singpass/login")
  public Map<String, String> loginWithSingPass() {
    return oAuth2LoginService.loginWithSingPass();
  }

  // add request param state later
  @PostMapping("login/oauth2/code/singpass")
  public Map<String, String> processAuthCodeAndExchangeToken(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response){
    Map<String, String> resultMap = new HashMap<>();
    try {
      resultMap = oAuth2LoginService.processAuthCodeAndExchangeToken(code);
      System.out.println("[ExchangeToken][Controller] resultMap code is: " + resultMap.get("code"));
      if (resultMap.get("code").equals(OK)) {
        request.getSession(true);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return resultMap;
  }
}
