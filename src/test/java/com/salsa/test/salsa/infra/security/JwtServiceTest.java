package com.salsa.test.salsa.infra.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

  private com.salsa.test.salsa.infra.security.JwtService jwtService;

  @BeforeEach
  void setUp() {
    jwtService = new com.salsa.test.salsa.infra.security.JwtService();
  }

  @Test
  void shouldGenerateAndValidateTokenSuccessfully() {
    String userId = "user123";
    String token = jwtService.generateToken(userId, "role");

    String extractedSubject = jwtService.validateAndGetSubject(token);

    assertEquals(userId, extractedSubject);
  }

  @Test
  void shouldThrowExceptionForInvalidToken() {
    String invalidToken = "ey.this.is.not.valid";

    Exception exception = assertThrows(RuntimeException.class, () -> {
      jwtService.validateAndGetSubject(invalidToken);
    });

    assertTrue(exception.getMessage().contains("Token JWT inválido"));
  }

}
