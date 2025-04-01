package com.salsa.test.salsa.infra.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

  // IMPORTANT: Replace this with a securely stored secret in production!
  private static final String SECRET = "this_is_a_super_secure_jwt_secret_key_123456";
  private static final long EXPIRATION_MS = 1000 * 60 * 60; // 1 hour

  private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

  /**
   * Generates a JWT token for a user with their role embedded.
   */
  public String generateToken(String userId, String role) {
    return Jwts.builder()
        .setSubject(userId)
        .claim("role", role)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * Validates a JWT and returns the user ID (subject).
   */
  public String validateAndGetSubject(String token) {
    try {
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody();
      return claims.getSubject();
    } catch (JwtException e) {
      throw new RuntimeException("Invalid JWT token", e);
    }
  }

  /**
   * Extracts the user's role from a JWT.
   */
  public String getRoleFromToken(String token) {
    try {
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody();
      return claims.get("role", String.class);
    } catch (JwtException e) {
      throw new RuntimeException("Unable to extract role from JWT token", e);
    }
  }
}