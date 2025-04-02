package com.salsa.test.salsa.infra.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

  private JwtService jwtService;
  private String userId;
  private String role;
  private String token;

  @BeforeEach
  void setUp() {
    jwtService = new JwtService();
    userId = "12345";
    role = "EMPLOYER";
    token = jwtService.generateToken(userId, role);
  }

  @Test
  void shouldGenerateValidToken() {
    assertNotNull(token);
    assertFalse(token.isBlank());
  }

  @Test
  void shouldValidateTokenAndReturnUserId() {
    String extractedId = jwtService.validateAndGetSubject(token);
    assertEquals(userId, extractedId);
  }

  @Test
  void shouldExtractRoleFromToken() {
    String extractedRole = jwtService.getRoleFromToken(token);
    assertEquals(role, extractedRole);
  }

  @Test
  void shouldThrowExceptionForInvalidToken() {
    String invalidToken = token + "tampered";

    RuntimeException exception = assertThrows(RuntimeException.class, () ->
        jwtService.validateAndGetSubject(invalidToken)
    );

    assertTrue(exception.getMessage().contains("Invalid JWT token"));
  }

  @Test
  void shouldThrowExceptionWhenGettingRoleFromInvalidToken() {
    String invalidToken = token + "broken";

    RuntimeException exception = assertThrows(RuntimeException.class, () ->
        jwtService.getRoleFromToken(invalidToken)
    );

    assertTrue(exception.getMessage().contains("Unable to extract role from JWT token"));
  }
}
