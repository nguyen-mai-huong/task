package com.ufinity.task.controller;

import com.ufinity.task.service.OAuth2LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    Map<String, String> redirectParamsMap = new HashMap<>();
    redirectParamsMap.put("authorization_uri", authorizationUri);
    redirectParamsMap.put("redirect_uri", redirectUri);
    return redirectParamsMap;
  }

  // add request param state later
  @GetMapping("login/oauth2/code/singpass")
  public void processAuthCodeAndExchangeToken(@RequestParam("code") String code){
    try {
      oAuth2LoginService.processAuthCodeAndExchangeToken(code);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
