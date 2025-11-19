package com.ashimeru.login_system.security.oauth;

import java.net.URI;
import java.util.Date;
import org.springframework.stereotype.Service;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Service
public class AppleJwtService {
  private JWKSet jwkSet;

  public AppleJwtService() {
    try {
      this.jwkSet =
      JWKSet.load(new URI("https://appleid.apple.com/auth/keys").toURL());
    } catch (Exception e) {
      throw new RuntimeException("Failed to load Apple JWKS", e);
    }
  }

  public JWTClaimsSet verifyAndDecode(String identityToken, String clientId)
      throws Exception {
  
    SignedJWT jwt = SignedJWT.parse(identityToken);
    JWSHeader header = jwt.getHeader();

    RSAKey key = (RSAKey) jwkSet.getKeyByKeyId(header.getKeyID());
    if (key == null) {
      throw new RuntimeException(
          "No matching Apple key found for kid: " + header.getKeyID());
    }

    RSASSAVerifier verifier = new RSASSAVerifier(key);
    if (!jwt.verify(verifier)) {
      throw new RuntimeException("Invalid Apple identity token signature");
    }
    
    JWTClaimsSet claims = jwt.getJWTClaimsSet();
    if (!"https://appleid.apple.com".equals(claims.getIssuer())) {
      throw new RuntimeException("Invalid issuer");
    }
    // if (!claims.getAudience().contains(clientId)) {
    //   throw new RuntimeException("Invalid audience");
    // }
    if (claims.getExpirationTime().before(new Date())) {
      throw new RuntimeException("Token expired");
    }

    return claims;
  }
}
