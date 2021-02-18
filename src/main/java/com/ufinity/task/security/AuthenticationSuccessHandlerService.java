package com.ufinity.task.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationSuccessHandlerService implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    String sessionId = request.getSession().getId();

    Cookie cookie = new Cookie("JSESSIONID", sessionId);
    response.addCookie(cookie);

    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");

    response.getWriter().write(sessionId);

    System.out.println("Con coc ngu ngu");
  }
}
