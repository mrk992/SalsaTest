package com.salsa.test.salsa.infra.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salsa.test.salsa.domain.model.UserRole;
import com.salsa.test.salsa.infra.entity.UserEntity;
import com.salsa.test.salsa.infra.persistence.repository.UserRepository;
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
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(AuthControllerTest.MockedBeans.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

  @MockitoBean
  private UserRepository userRepository;

  @MockitoBean
  private JwtService jwtService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldLoginSuccessfully() throws Exception {
    UserEntity user = new UserEntity("1", "empleador1", "password", UserRole.EMPLOYER);
    when(userRepository.findByUsername("empleador1")).thenReturn(Optional.of(user));
    when(jwtService.generateToken("1", "EMPLOYER")).thenReturn("mocked-jwt");

    LoginRequest request = new LoginRequest("empleador1", "password");

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("mocked-jwt"))
        .andExpect(jsonPath("$.role").value("EMPLOYER"));
  }

  @Test
  void shouldReturnUnauthorizedForWrongPassword() throws Exception {
    UserEntity user = new UserEntity("1", "empleador1", "wrongpass", UserRole.EMPLOYER);
    when(userRepository.findByUsername("empleador1")).thenReturn(Optional.of(user));

    LoginRequest request = new LoginRequest("empleador1", "password");

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.error").value("User or pass incorrect"));
  }

  @Test
  void shouldReturnUnauthorizedForNonexistentUser() throws Exception {
    when(userRepository.findByUsername("noUser")).thenReturn(Optional.empty());

    LoginRequest request = new LoginRequest("noUser", "password");

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