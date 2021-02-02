package com.ufinity.task.controller;

import com.ufinity.task.service.OAuth2LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OAuth2LoginController {

  @Value("${oauth2.client.registration.singpass.client-id}")
  private String clientId;

  @Value("${oauth2.client.registration.singpass.client-secret}")
  private String clientSecret;

  @Value("${oauth2.client.registration.singpass.authorization-uri}")
  private String authorizationUri;

  @Value("${oauth2.client.registration.singpass.redirect-uri}")
  private String redirectUri;

  @Autowired
  private OAuth2LoginService oAuth2LoginService;

  @GetMapping("singpass/login")
  public Map<String, String> loginWithSingPass() {
    return oAuth2LoginService.loginWithSingPass();
  }

  // add request param state later
  @GetMapping("login/oauth2/code/singpass")
  public void processAuthCodeAndExchangeToken(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response){
    try {
      boolean canCreateSession =  oAuth2LoginService.processAuthCodeAndExchangeToken(code);
      if (canCreateSession) {
        request.getSession(true);
        response.sendRedirect("http://localhost:3000/waiting");
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
