package com.salsa.test.salsa.infra.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salsa.test.salsa.infra.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private ObjectMapper objectMapper;
/*
  private JobOffer buildJobOffer() {
    return new JobOffer(
        UUID.randomUUID(),
        "Backend Developer",
        "TechCorp",
        "Madrid",
        "Spring Boot, Java 21",
        JobType.FULL_TIME,
        new SalaryRange(3000, 5000),
        List.of("Gym", "Lunch"),
        List.of("Stock options"),
        null
    );
  }

  @Test
  void employeeShouldNotBeAllowedToCreateJobOffer() throws Exception {
    String token = jwtService.generateToken("employee1", "EMPLOYEE");

    mockMvc.perform(post("/api/job-offers")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(buildJobOffer())))
        .andExpect(status().isForbidden());
  }

  @Test
  void employerShouldBeAllowedToCreateJobOffer() throws Exception {
    String token = jwtService.generateToken("employer1", "EMPLOYER");

    mockMvc.perform(post("/api/job-offers")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(buildJobOffer())))
        .andExpect(status().isOk());
  }

  @Test
  void employeeShouldNotBeAllowedToDeleteJobOffer() throws Exception {
    String token = jwtService.generateToken("employee1", "EMPLOYEE");
    UUID randomId = UUID.randomUUID();

    mockMvc.perform(post("/api/job-offers/" + randomId)
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isMethodNotAllowed()); // Prevent accidental POST to /{id}

    mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
            .delete("/api/job-offers/" + randomId)
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isForbidden());
  }

  @Test
  void employerShouldBeAllowedToDeleteJobOffer() throws Exception {
    String token = jwtService.generateToken("employer1", "EMPLOYER");

    // Create offer first
    String content = objectMapper.writeValueAsString(buildJobOffer());
    String idResponse = mockMvc.perform(post("/api/job-offers")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andReturn()
        .getResponse()
        .getContentAsString();

    UUID createdId = UUID.fromString(idResponse.replace("\"", ""));

    // Now delete it
    mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
            .delete("/api/job-offers/" + createdId)
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isNoContent());
  }

  @Test
  void employeeShouldBeAllowedToSearchJobOffers() throws Exception {
    String token = jwtService.generateToken("employee1", "EMPLOYEE");

    mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
            .get("/api/job-offers/search")
            .param("title", "developer")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
  }

  @Test
  void unauthenticatedUserShouldBeRejected() throws Exception {
    mockMvc.perform(post("/api/job-offers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(buildJobOffer())))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
            .get("/api/job-offers/search"))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
            .delete("/api/job-offers/" + UUID.randomUUID()))
        .andExpect(status().isUnauthorized());
  }*/
}
