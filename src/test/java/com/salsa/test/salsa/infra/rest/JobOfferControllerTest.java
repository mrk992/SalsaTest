package com.salsa.test.salsa.infra.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salsa.test.salsa.application.JobOfferService;
import com.salsa.test.salsa.domain.model.JobOffer;
import com.salsa.test.salsa.domain.model.SalaryRange;
import com.salsa.test.salsa.domain.model.SearchCriteria;
import com.salsa.test.salsa.infra.rest.dto.JobOfferRequest;
import com.salsa.test.salsa.infra.rest.dto.JobOfferResponse;
import com.salsa.test.salsa.infra.rest.mapper.JobOfferWebMapper;
import com.salsa.test.salsa.infra.security.JwtAuthFilter;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.salsa.test.salsa.domain.model.JobType.FULL_TIME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobOfferController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(JobOfferControllerTest.MockBeans.class)
class JobOfferControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private JobOfferService jobOfferService;

  @MockitoBean
  private JobOfferWebMapper webMapper;

  @Test
  void shouldReturnJobOffersPage() throws Exception {
    JobOffer jobOffer = new JobOffer(UUID.randomUUID(), "Java Dev", UUID.randomUUID(), "Company", "Remote", "Desc", FULL_TIME, null, List.of(), List.of(), LocalDateTime.now());
    JobOfferResponse response = new JobOfferResponse(UUID.randomUUID(), "Java Dev", UUID.randomUUID(), "Company", "Remote", "FULL_TIME", FULL_TIME, 0L, 0L, List.of(), List.of(), LocalDateTime.now());

    when(jobOfferService.search(any(SearchCriteria.class), any())).thenReturn(new PageImpl<>(List.of(jobOffer)));
    when(webMapper.toResponse(any(JobOffer.class))).thenReturn(response);

    mockMvc.perform(get("/api/job-offers"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].jobTitle").value("Java Dev"));
  }

  @Test
  void shouldReturnJobOfferById() throws Exception {
    UUID id = UUID.randomUUID();
    JobOffer jobOffer = new JobOffer(id, "Backend", UUID.randomUUID(), "Company", "Remote", "Desc", FULL_TIME, new SalaryRange(1L, 2L), List.of(), List.of(), LocalDateTime.now());
    JobOfferResponse response = new JobOfferResponse(id, "Backend", UUID.randomUUID(), "Company", "Remote", "FULL_TIME", FULL_TIME, 0L, 0L, List.of(), List.of(), LocalDateTime.now());

    when(jobOfferService.findById(id)).thenReturn(Optional.of(jobOffer));
    when(webMapper.toResponse(jobOffer)).thenReturn(response);

    mockMvc.perform(get("/api/job-offers/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.jobTitle").value("Backend"));
  }

  @Test
  void shouldReturnNotFoundWhenJobOfferDoesNotExist() throws Exception {
    UUID id = UUID.randomUUID();
    when(jobOfferService.findById(id)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/job-offers/" + id))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldCreateJobOffer() throws Exception {
    JobOfferRequest request = new JobOfferRequest("Backend", UUID.randomUUID(), "Company", "Desc", "FULL_TIME", FULL_TIME, 1L, 2L, List.of(), List.of());
    JobOffer jobOffer = new JobOffer(UUID.randomUUID(), "Backend", UUID.randomUUID(), "Company", "Remote", "Desc", FULL_TIME, null, List.of(), List.of(), LocalDateTime.now());
    JobOfferResponse response = new JobOfferResponse(jobOffer.id(), "Backend", jobOffer.companyId(), "Company", "Remote", "FULL_TIME", FULL_TIME, 0L, 0L, List.of(), List.of(), jobOffer.createdAt());

    when(webMapper.toDomain(request)).thenReturn(jobOffer);
    when(webMapper.toResponse(jobOffer)).thenReturn(response);

    mockMvc.perform(post("/api/job-offers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @TestConfiguration
  static class MockBeans {
    @Bean
    public RateLimitFilter rateLimitFilter() {
      return Mockito.mock(RateLimitFilter.class);
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
      return Mockito.mock(JwtAuthFilter.class);
    }

    @Bean
    public JwtService jwtService() {
      return Mockito.mock(JwtService.class);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
      return Mockito.mock(StringRedisTemplate.class);
    }
  }
}
