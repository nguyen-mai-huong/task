package com.ufinity.task.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Base64;

public class TokenUtils {

  // Reference: auth0 java-jwt
  public static boolean verifySignature(String token) throws InvalidKeyException, CertificateException, FileNotFoundException, NoSuchAlgorithmException, SignatureException {
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

  public static String getSubject(String payload) throws Exception {
    JsonNode payloadJson = JsonUtils.convertToJsonNode(payload);
    String nric = payloadJson.get("sub").textValue().split(",")[0].substring(2);
    return nric;
  }

}
