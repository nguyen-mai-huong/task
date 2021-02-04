package com.ufinity.task.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufinity.task.utils.JsonUtils;
import com.ufinity.task.utils.TokenUtils;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
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
    System.out.println(String.format("[TokenExchangeRequest] Response: %d, %s", tokenExchangeRequestResponse.statusCode(), tokenExchangeRequestResponse.body()));

    JsonNode node = JsonUtils.convertToJsonNode((tokenExchangeRequestResponse.body()));

    // Enable signing and try to get id token
    String idToken = node.get("id_token").textValue();
    boolean isValidSignature = TokenUtils.verifySignature(idToken);
    System.out.println("DEBUG 06: is valid signature " + isValidSignature);

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

    // Subject is: s=T0066846F,u=4
    String nric = TokenUtils.getSubject(payload);
    System.out.println("[TokenExchangeRequest] NRIC is: " + nric);

    HttpRequest getUserInfoRequest = prepareGetUserInfoRequest(nric);

    HttpResponse<String> userInfoRequestResponse = client.send(getUserInfoRequest, HttpResponse.BodyHandlers.ofString());
    System.out.println(String.format("[UserInfoRequest] Response is: %d, %s", userInfoRequestResponse.statusCode(), userInfoRequestResponse.body()));

    // For this get user details info, if i get back code 200 or 404 meaning OK already
    // I should go and create session for this user.
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

    System.out.println("[TokenExchangeRequest] JSON request body: " + requestBody);

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(tokenUri))
            .headers("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();
    return request;
  }

  private HttpRequest prepareGetUserInfoRequest(String nric) throws IOException {
    String token = buildJWTToken(nric);

    // Send request to http://localhost:5156/myinfo/{v2,v3}/person
    HttpRequest getUserInfoRequest = HttpRequest.newBuilder()
            .uri(URI.create(String.format("http://localhost:5156/myinfo/v2/person/%s", nric)))
            .headers("Authorization", token)
            .GET()
            .build();
    return getUserInfoRequest;
  }

  private String buildJWTToken(String subject) throws IOException {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    System.out.println("[UserInfoRequest][JWTToken] Current directory: " + System.getProperty("user.dir"));
    String pathToSecretKey = System.getProperty("user.dir") + System.getProperty("file.separator") + "src" + System.getProperty("file.separator") + "main" + System.getProperty("file.separator") + "resources" + System.getProperty("file.separator") + "spcp-key.pem";

    Path path = Paths.get(pathToSecretKey);
    byte[] secretKey = Files.readAllBytes(path);

    Key signingKey = new SecretKeySpec(secretKey, signatureAlgorithm.getJcaName());

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

    System.out.println("[UserInfoRequest][JWTToken] The token is " + token);
    return token;
  }


  // Reference: auth0 java-jwt
  private boolean verifySignature(String token) throws InvalidKeyException, CertificateException, FileNotFoundException, NoSuchAlgorithmException, SignatureException {
    String[] parts = token.trim().split("\\.");
    if (parts == null || parts.length != 3) {
      return false;
    }

    String pathToPublicKey = System.getProperty("user.dir") + System.getProperty("file.separator") + "src" + System.getProperty("file.separator") + "main" + System.getProperty("file.separator") + "resources" + System.getProperty("file.separator") + "spcp.crt";
    Path path = Paths.get(pathToPublicKey);

    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
    Certificate certificate = certificateFactory.generateCertificate(new FileInputStream(path.toFile()));
    PublicKey publicKey = certificate.getPublicKey();

    Signature signature = Signature.getInstance("SHA256withRSA");
    signature.initVerify(publicKey);
    signature.update(parts[0].getBytes(StandardCharsets.UTF_8));
    signature.update((byte) 46);
    signature.update(parts[1].getBytes(StandardCharsets.UTF_8));

    byte[] tokenSignature = Base64.getUrlDecoder().decode(parts[2].getBytes(StandardCharsets.UTF_8));
    return signature.verify(tokenSignature);
  }

  private boolean verifyClaims(String payload) throws Exception {
    JsonNode payloadJson = JsonUtils.convertToJsonNode(payload);
    String issuer = payloadJson.get("iss").textValue();

    return issuer.equals(authorizationServerId);
  }

  private String getSubject(String payload) throws Exception {
    JsonNode payloadJson = JsonUtils.convertToJsonNode(payload);
    String nric = payloadJson.get("sub").textValue().split(",")[0].substring(2);
    System.out.println("[TokenExchangeRequest] NRIC is: " + nric);
    return nric;
  }


}
