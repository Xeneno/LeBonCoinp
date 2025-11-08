package com.example.LeBonCoinp.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
  private final SecretKey key;
  private final String issuer;
  private final long accessMinutes;

  public JwtService(
      @Value("${security.jwt.secret}") String secret,
      @Value("${security.jwt.issuer}") String issuer,
      @Value("${security.jwt.access-token-minutes}") long accessMinutes) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    this.issuer = issuer;
    this.accessMinutes = accessMinutes;
  }

  public String generate(String subject, Map<String,Object> claims) {
    Instant now = Instant.now();
    return Jwts.builder()
        .issuer(issuer)
        .subject(subject)
        .claims(claims)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(accessMinutes * 60)))
        .signWith(key)
        .compact();
  }

  public boolean isValid(String token) {
    try {
      Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public String subject(String token) {
    return Jwts.parser().verifyWith(key).build()
        .parseSignedClaims(token).getPayload().getSubject();
  }
}
