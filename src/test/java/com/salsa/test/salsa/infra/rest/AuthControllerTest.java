package com.salsa.test.salsa.infra.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salsa.test.salsa.application.AuthService;
import com.salsa.test.salsa.infra.rest.dto.LoginRequest;
import com.salsa.test.salsa.infra.security.JwtService;
import com.salsa.test.salsa.infra.security.RateLimitFilter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(AuthControllerTest.MockedBeans.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private AuthService authService;

  @MockitoBean
  private JwtService jwtService;

  @Test
  void shouldReturnTokenWhenLoginIsSuccessful() throws Exception {
    LoginRequest request = new LoginRequest("empleador1", "password");

    ResponseEntity<Map<String, String>> response = ResponseEntity.ok(Map.of(
        "token", "mocked-jwt-token",
        "role", "EMPLOYER"
    ));

    when(authService.login(any(LoginRequest.class))).thenReturn(response);

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
        .andExpect(jsonPath("$.role").value("EMPLOYER"));
  }

  @Test
  void shouldReturnUnauthorizedWhenLoginFails() throws Exception {
    LoginRequest request = new LoginRequest("empleador1", "wrongpassword");

    when(authService.login(any())).thenReturn(ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(Map.of("error", "User or pass incorrect")));

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.error").value("User or pass incorrect"));
  }

  @TestConfiguration
  static class MockedBeans {

    @Bean
    public RateLimitFilter rateLimitFilter() {
      return Mockito.mock(RateLimitFilter.class);
    }

    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
      http.csrf().disable()
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/auth/login").permitAll()
              .anyRequest().authenticated()
          );
      return http.build();
    }
  }
}
