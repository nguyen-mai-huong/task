package com.ufinity.task.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.ufinity.task.utils.JsonUtils;
import com.ufinity.task.utils.TokenUtils;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.ufinity.task.utils.Constants.ERROR;
import static com.ufinity.task.utils.Constants.OAUTH2_INVALID_CLAIMS;
import static com.ufinity.task.utils.Constants.OAUTH2_INVALID_SIGNATURE;
import static com.ufinity.task.utils.Constants.OK;

@Service
public class OAuth2LoginService {

  @Value("${oauth2.client.registration.singpass.client-id}")
  private String clientId;

  @Value("${oauth2.client.registration.singpass.client-secret}")
  private String clientSecret;

  @Value("${oauth2.client.registration.singpass.authorization-uri}")
  private String authorizationUri;

  @Value("${oauth2.client.registration.singpass.redirect-uri}")
  private String redirectUri;

  @Value("${oauth2.client.registration.singpass.token-uri}")
  private String tokenUri;

  @Value("${oauth2.client.registration.singpass}")
  private String authorizationServerId;

  public Map<String, String> loginWithSingPass() {
    Map<String, String> redirectParamsMap = new HashMap<>();
    redirectParamsMap.put("authorization_uri", authorizationUri);
    redirectParamsMap.put("redirect_uri", redirectUri);
    return redirectParamsMap;
  }

  public Map<String, String> processAuthCodeAndExchangeToken(String code) throws Exception{
    Map<String, String> resultMap = new HashMap<>();

    HttpClient client = HttpClient.newHttpClient();

    HttpRequest tokenExchangeRequest = prepareTokenExchangeRequest(code);

    HttpResponse<String> tokenExchangeRequestResponse = client.send(tokenExchangeRequest, HttpResponse.BodyHandlers.ofString());

    JsonNode node = JsonUtils.convertToJsonNode((tokenExchangeRequestResponse.body()));

    String idToken = node.get("id_token").textValue();
    boolean isValidSignature = TokenUtils.verifySignature(idToken);

    if (!isValidSignature) {
      resultMap.put("code", ERROR);
      resultMap.put("error", OAUTH2_INVALID_SIGNATURE);
      return resultMap;
    }

    String encodedPayload = idToken.trim().split("\\.")[1];
    String payload = new String(Base64.getDecoder().decode(encodedPayload), StandardCharsets.UTF_8);
    boolean isValidClaim = verifyClaims(payload);
    if (!isValidClaim) {
      resultMap.put("code", ERROR);
      resultMap.put("error", OAUTH2_INVALID_CLAIMS);
      return resultMap;
    }

    String nric = TokenUtils.getSubject(payload);

    HttpRequest getUserInfoRequest = prepareGetUserInfoRequest(nric);

    HttpResponse<String> userInfoRequestResponse = client.send(getUserInfoRequest, HttpResponse.BodyHandlers.ofString());

    // After verifying signature, sending request to fetch user details info
    // Code 404 is OK as MockPass sometimes returns 404 - User doesn't have info in MyInfo
    boolean canCreateSession = userInfoRequestResponse.statusCode() == 200 || userInfoRequestResponse.statusCode() == 404;

    if (canCreateSession) {
      resultMap.put("code", OK);
      resultMap.put("username", nric);
      resultMap.put("idToken", idToken);
    } else {
      resultMap.put("code", ERROR);
    }

    return resultMap;
  }

  private HttpRequest prepareTokenExchangeRequest(String code) {
    Map<String, String> parameters = new HashMap<>();
    parameters.put("client_id", clientId);
    parameters.put("code", code);

    String requestBody = "";
    for (String key : parameters.keySet()) {
      String encodedParam = String.format("%s=%s&", key, URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8));
      requestBody += encodedParam;
    }

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(tokenUri))
            .headers("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();
    return request;
  }

  private HttpRequest prepareGetUserInfoRequest(String nric) {
    String token = buildJWTToken(nric);

    HttpRequest getUserInfoRequest = HttpRequest.newBuilder()
            .uri(URI.create(String.format("http://localhost:5156/myinfo/v2/person/%s", nric)))
            .headers("Authorization", token)
            .GET()
            .build();
    return getUserInfoRequest;
  }

  private String buildJWTToken(String subject) {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    Date now = new Date();

    String encodedKey = Base64.getEncoder().encodeToString("secret".getBytes(StandardCharsets.UTF_8));

    Map<String, Object> claims = new HashMap<>();
    claims.put("scope", "openid");
    claims.put("sub", subject);

    JwtBuilder builder = Jwts.builder().setId("id").setIssuedAt(now)
            .setSubject(subject)
            .setClaims(claims)
            .signWith(signatureAlgorithm, encodedKey);
    String token = builder.compact();

    return token;
  }

  private boolean verifyClaims(String payload) throws Exception {
    JsonNode payloadJson = JsonUtils.convertToJsonNode(payload);
    String issuer = payloadJson.get("iss").textValue();

    boolean isFromValidAuthorisationServer = issuer.equals(authorizationServerId);
    boolean isValidSubject = TokenUtils.isValidSubject(payload);

    return isFromValidAuthorisationServer && isValidSubject;
  }

}
