package com.salsa.test.salsa.infra.rest;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class JobOfferControllerTest {
/*
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private JobOffer exampleOffer;

  @BeforeEach
  void setup() {
    exampleOffer = new JobOffer(
        UUID.randomUUID(),
        "Backend Developer",
        "TechCorp",
        "Barcelona",
        "Desarrollo backend con Java",
        JobType.FULL_TIME,
        new SalaryRange(3000, 4500),
        List.of("Seguro médico", "Comida gratis"),
        List.of("Opción de stock"),
        null // se seteará en el servicio
    );
  }

  @Test
  void shouldCreateAndGetJobOffer() throws Exception {
    // Crear oferta
    String json = objectMapper.writeValueAsString(exampleOffer);
    String response = mockMvc.perform(post("/api/job-offers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    UUID createdId = UUID.fromString(response.replace("\"", ""));

    // Obtener la oferta creada
    mockMvc.perform(get("/api/job-offers/" + createdId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.jobTitle").value("Backend Developer"));
  }

  @Test
  void shouldReturnNotFoundForInvalidId() throws Exception {
    mockMvc.perform(get("/api/job-offers/" + UUID.randomUUID()))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnAllJobOffers() throws Exception {
    mockMvc.perform(get("/api/job-offers"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldDeleteJobOffer() throws Exception {
    String json = objectMapper.writeValueAsString(exampleOffer);
    String response = mockMvc.perform(post("/api/job-offers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andReturn().getResponse().getContentAsString();

    UUID createdId = UUID.fromString(response.replace("\"", ""));

    mockMvc.perform(delete("/api/job-offers/" + createdId))
        .andExpect(status().isNoContent());
  }*/
}