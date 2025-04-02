package com.salsa.test.salsa.infra.rest;


import com.salsa.test.salsa.application.AuthService;
import com.salsa.test.salsa.infra.rest.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {


  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String,String>> login(@RequestBody LoginRequest request) {
    return authService.login(request);
  }
}