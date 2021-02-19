package com.ufinity.task.security;

import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ufinity.task.utils.Constants.OK;

@Service
public class AuthenticationSuccessHandlerService implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    String sessionId = request.getSession().getId();

    JSONObject json = new JSONObject();
    json.put("code", OK);
    json.put("sessionId", sessionId);
    if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("SYS_ADMIN"))) {
      json.put("isAdmin", true);
    } else {
      json.put("isAdmin", false);
    }

    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(json.toString());

  }
}
