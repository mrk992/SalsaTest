package com.salsa.test.salsa.infra.rest;


import com.salsa.test.salsa.infra.persistence.mapper.UserMapper;
import com.salsa.test.salsa.infra.persistence.repository.UserRepository;
import com.salsa.test.salsa.infra.rest.dto.LoginRequest;
import com.salsa.test.salsa.infra.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final UserRepository userRepository;
  private final JwtService jwtService;

  public AuthController(UserRepository userRepository, JwtService jwtService) {
    this.userRepository = userRepository;
    this.jwtService = jwtService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    return userRepository.findByUsername(request.username())
        .map(UserMapper::toDomain)
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