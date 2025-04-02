package com.salsa.test.salsa.application;

import com.salsa.test.salsa.domain.port.AuthPort;
import com.salsa.test.salsa.infra.rest.dto.LoginRequest;
import com.salsa.test.salsa.infra.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class AuthService {

  private final AuthPort authPort;
  private final JwtService jwtService;

  public AuthService(AuthPort authPort, JwtService jwtService) {
    this.authPort = authPort;
    this.jwtService = jwtService;
  }

  public ResponseEntity<Map<String,String>> login(LoginRequest request) {
    return authPort.findByUsername(request.username())
        .filter(user -> user.getPassword().equals(request.password()))
        .map(user -> {
          String token = jwtService.generateToken(user.getId(), user.getRole().name());
          return ResponseEntity.ok(Map.of(
              "token", token,
              "role", user.getRole().name()
          ));
        })
        .orElse(ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "User or pass incorrect")));
  }
}
