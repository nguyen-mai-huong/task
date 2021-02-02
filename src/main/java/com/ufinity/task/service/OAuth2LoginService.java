package com.ufinity.task.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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

  public boolean processAuthCodeAndExchangeToken(String code) throws Exception{
    Map<String, String> parameters = new HashMap<>();
    parameters.put("client_id", clientId);
    parameters.put("code", code);

    String requestBody = "";
    for(String key : parameters.keySet()) {
      String encodedParam = String.format("%s=%s&", key, URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8));
      requestBody += encodedParam;
    }

    System.out.println("DBUG 03: JSON request body" + requestBody);



    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(tokenUri))
            .headers("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    System.out.println(String.format("Resonse: %d, %s", response.statusCode(), response.body()));

    JsonNode node = convertToJsonNode((response.body()));

    System.out.println("Subject is: " + node.get("id_token").get("sub").textValue());

    // Subject is: s=T0066846F,u=4
    String nric = node.get("id_token").get("sub").textValue().split(",")[0].substring(2);
    System.out.println("NRIC is: " + nric);

    String token = buildJWTToken(nric);
    // Send request to http://localhost:5156/myinfo/{v2,v3}/person
    HttpRequest getUserInfo = HttpRequest.newBuilder()
            .uri(URI.create(String.format("http://localhost:5156/myinfo/v2/person/%s", nric)))
            .headers("Authorization", token)
            .GET()
            .build();

    HttpResponse<String> response2 = client.send(getUserInfo, HttpResponse.BodyHandlers.ofString());
    // CompletableFuture<HttpResponse<String>> response2 = client.sendAsync(getUserInfo, HttpResponse.BodyHandlers.ofString());
    System.out.println(String.format("Resonse: %d, %s", response2.statusCode(), response2.body()));
    // String result = response2.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
    // System.out.println(String.format("Get user detail info result: %s", result));


    // For this get user details info, if i get back code 200 or 404 meaning OK already
    // I should go and create session for this user.
    boolean canCreateSession = response2.statusCode() == 200 || response2.statusCode() == 404;

    return canCreateSession;


  }



  private JsonNode convertToJsonNode(String jsonString) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readTree(jsonString);
  }

  private String buildJWTToken(String nric) throws IOException {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    System.out.println("DEBUG 04: " + System.getProperty("user.dir"));
    String pathToSecretKey = System.getProperty("user.dir") + System.getProperty("file.separator") + "src" + System.getProperty("file.separator") + "main" + System.getProperty("file.separator") + "resources" + System.getProperty("file.separator") + "spcp-key.pem";

    Path path = Paths.get(pathToSecretKey);
    // File theFile = path.toFile();
    // System.out.println("File path and Does file exist? " + theFile.getAbsolutePath() + ", " + theFile.exists());
    byte[] secretKey = Files.readAllBytes(path);

    Key signingKey = new SecretKeySpec(secretKey, signatureAlgorithm.getJcaName());

    Date now = new Date();

    String encodedKey = Base64.getEncoder().encodeToString("secret".getBytes(StandardCharsets.UTF_8));

    Map<String, Object> claims = new HashMap<>();
    claims.put("scope", "openid");
    claims.put("sub", nric);

    // Using sign with a weak secret first
    JwtBuilder builder = Jwts.builder().setId("id").setIssuedAt(now)
            .setSubject(nric)
            .setClaims(claims)
            .signWith(signatureAlgorithm, encodedKey);
    String token = builder.compact();

    System.out.println("DEBUG 05: The token is " + token);



    return token;
  }
}
