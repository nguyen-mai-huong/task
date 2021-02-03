package com.ufinity.task.utils;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {

  public static String buildJWTTokenSignWithSecret(String subject) {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    Date now = new Date();

    String encodedKey = Base64.getEncoder().encodeToString("secret".getBytes(StandardCharsets.UTF_8));

    Map<String, Object> claims = new HashMap<>();
    claims.put("sub", subject);

    JwtBuilder builder = Jwts.builder().setId("id").setIssuedAt(now)
            .setClaims(claims)
            .signWith(signatureAlgorithm, encodedKey);
    String token = builder.compact();

    System.out.println("[TokenUtils] The token is " + token);
    return token;
  }

}
