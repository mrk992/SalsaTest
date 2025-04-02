package com.salsa.test.salsa.application;

import com.salsa.test.salsa.domain.model.JobOffer;
import com.salsa.test.salsa.domain.model.SearchCriteria;
import com.salsa.test.salsa.domain.port.JobOfferPort;
import com.salsa.test.salsa.domain.service.JobOfferRankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JobOfferServiceTest {

  @Mock
  private JobOfferPort jobOfferPort;

  @Mock
  private JobOfferRankingService rankingService;

  @InjectMocks
  private JobOfferService jobOfferService;

  private JobOffer sampleOffer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    sampleOffer = new JobOffer(
        UUID.randomUUID(),
        "Dev",
        UUID.randomUUID(),
        "Company",
        "Location",
        "Description",
        null,
        null,
        List.of(),
        List.of(),
        null
    );
  }

  @Test
  void shouldCreateJobOffer() {
    jobOfferService.create(sampleOffer);
    verify(jobOfferPort).save(sampleOffer);
  }

  @Test
  void shouldFindJobOfferById() {
    UUID id = sampleOffer.id();
    when(jobOfferPort.findById(id)).thenReturn(Optional.of(sampleOffer));

    Optional<JobOffer> result = jobOfferService.findById(id);
    assertTrue(result.isPresent());
    assertEquals(sampleOffer, result.get());
  }

  @Test
  void shouldFindAllJobOffersAndRankThem() {
    List<JobOffer> offers = List.of(sampleOffer);
    when(jobOfferPort.findAll()).thenReturn(offers);
    when(rankingService.rank(offers)).thenReturn(offers);

    List<JobOffer> result = jobOfferService.findAll();
    assertEquals(offers, result);
  }

  @Test
  void shouldDeleteJobOfferById() {
    UUID id = sampleOffer.id();
    jobOfferService.delete(id);
    verify(jobOfferPort).deleteById(id);
  }

  @Test
  void shouldSearchAndRankOffersWithPagination() {
    Pageable pageable = PageRequest.of(0, 1);
    List<JobOffer> offers = List.of(sampleOffer);
    Page<JobOffer> page = new PageImpl<>(offers, pageable, 1);

    when(jobOfferPort.search(any(), eq(pageable))).thenReturn(page);
    when(rankingService.rank(offers)).thenReturn(offers);

    Page<JobOffer> result = jobOfferService.search(new SearchCriteria("title", "loc", 1000.0), pageable);

    assertEquals(1, result.getTotalElements());
    assertEquals(sampleOffer, result.getContent().get(0));
  }
}
