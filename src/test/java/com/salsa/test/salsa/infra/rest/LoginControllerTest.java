package com.salsa.test.salsa.infra.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldLoginSuccessfullyAndReturnToken() throws Exception {
    mockMvc.perform(post("/auth/login")
            .param("username", "empleador1")
            .param("password", "password"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.role").value("EMPLOYER"));
  }

  @Test
  void shouldRejectInvalidCredentials() throws Exception {
    mockMvc.perform(post("/auth/login")
            .param("username", "empleador1")
            .param("password", "wrongPassword"))
        .andExpect(status().isUnauthorized())
        .andExpect(content().string(containsString("Usuario o contraseña incorrectos")));
  }
}

