package com.salsa.test.salsa.application;

class JobOfferServiceTest {
/*
  private JobOfferPort repository;
  private JobOfferService service;

  @BeforeEach
  void setUp() {
    repository = mock(JobOfferPort.class);
    service = new JobOfferService(repository);
  }

  @Test
  void shouldCreateJobOfferWithNewIdAndSaveIt() {
    JobOffer offer = createOffer(null);
    UUID generatedId = service.createJobOffer(offer);

    assertNotNull(generatedId);
    verify(repository).save(any(JobOffer.class));
  }

  @Test
  void shouldReturnOfferWhenFoundById() {
    UUID id = UUID.randomUUID();
    JobOffer offer = createOffer(id);

    when(repository.findById(id)).thenReturn(Optional.of(offer));

    Optional<JobOffer> result = service.getById(id);

    assertTrue(result.isPresent());
    assertEquals(id, result.get().getId());
  }

  @Test
  void shouldDeleteOfferById() {
    UUID id = UUID.randomUUID();
    service.delete(id);

    verify(repository).deleteById(id);
  }

  @Test
  void shouldReturnOffersOrderedByRankingRules() {
    JobOffer recentHighSalary = createOfferWithParams("A", "CompanyX", 2000, 3000, LocalDateTime.now().minusDays(2));
    JobOffer olderLowSalary = createOfferWithParams("B", "CompanyX", 1000, 1500, LocalDateTime.now().minusDays(10));
    JobOffer recentLowSalary = createOfferWithParams("C", "CompanyY", 1000, 1500, LocalDateTime.now().minusDays(1));

    List<JobOffer> offers = List.of(recentHighSalary, olderLowSalary, recentLowSalary);
    when(repository.search(any())).thenReturn(offers);

    List<JobOffer> result = service.search(new SearchCriteria(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));

    assertEquals("A", result.get(0).getJobTitle()); // reciente + mejor salario
    assertEquals("C", result.get(1).getJobTitle()); // reciente
    assertEquals("B", result.get(2).getJobTitle()); // más viejo
  }

  // Helpers

  private JobOffer createOffer(UUID id) {
    return new JobOffer(
        id,
        "Backend Dev",
        "ACME",
        "Madrid",
        "Descripción",
        JobType.FULL_TIME,
        new SalaryRange(1500, 2500),
        List.of("Café"),
        List.of("Gaming Fridays"),
        LocalDateTime.now()
    );
  }

  private JobOffer createOfferWithParams(String title, String company, double minSalary, double maxSalary, LocalDateTime createdAt) {
    return new JobOffer(
        UUID.randomUUID(),
        title,
        company,
        "Madrid",
        "Descripción",
        JobType.FULL_TIME,
        new SalaryRange(minSalary, maxSalary),
        List.of(),
        List.of(),
        createdAt
    );
  }*/
}
