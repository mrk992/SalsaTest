package com.salsa.test.salsa.application;

import com.salsa.test.salsa.domain.model.User;
import com.salsa.test.salsa.domain.model.UserRole;
import com.salsa.test.salsa.domain.port.AuthPort;
import com.salsa.test.salsa.infra.rest.dto.LoginRequest;
import com.salsa.test.salsa.infra.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

  private AuthPort authPort;
  private JwtService jwtService;
  private AuthService authService;

  @BeforeEach
  void setUp() {
    authPort = mock(AuthPort.class);
    jwtService = mock(JwtService.class);
    authService = new AuthService(authPort, jwtService);
  }

  @Test
  void shouldReturnTokenWhenCredentialsAreValid() {
    // Given
    LoginRequest request = new LoginRequest("empleador1", "password");
    User user = new User("1", "empleador1", "password", UserRole.EMPLOYER);

    when(authPort.findByUsername("empleador1")).thenReturn(Optional.of(user));
    when(jwtService.generateToken("1", "EMPLOYER")).thenReturn("mocked-token");

    // When
    ResponseEntity<Map<String, String>> response = authService.login(request);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("mocked-token", response.getBody().get("token"));
    assertEquals("EMPLOYER", response.getBody().get("role"));
  }

  @Test
  void shouldReturnUnauthorizedWhenUserNotFound() {
    LoginRequest request = new LoginRequest("noUser", "password");

    when(authPort.findByUsername("noUser")).thenReturn(Optional.empty());

    ResponseEntity<Map<String, String>> response = authService.login(request);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("User or pass incorrect", response.getBody().get("error"));
  }

  @Test
  void shouldReturnUnauthorizedWhenPasswordIsWrong() {
    User user = new User("1", "empleador1", "correctpass", UserRole.EMPLOYER);
    LoginRequest request = new LoginRequest("empleador1", "wrongpass");

    when(authPort.findByUsername("empleador1")).thenReturn(Optional.of(user));

    ResponseEntity<Map<String, String>> response = authService.login(request);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("User or pass incorrect", response.getBody().get("error"));
  }
}
