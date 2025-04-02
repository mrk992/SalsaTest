package com.salsa.test.salsa.domain.rules;

import com.salsa.test.salsa.domain.model.JobOffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompanyPopularityRuleTest {

  private CompanyPopularityRule rule;

  private UUID companyA;
  private UUID companyB;
  private JobOffer offer1;
  private JobOffer offer2;
  private JobOffer offer3;

  @BeforeEach
  void setUp() {
    rule = new CompanyPopularityRule();

    companyA = UUID.randomUUID();
    companyB = UUID.randomUUID();

    offer1 = new JobOffer(UUID.randomUUID(), "Dev A1", companyA, "Company A", "Loc", "Desc", null, null, List.of(), List.of(), LocalDateTime.now());
    offer2 = new JobOffer(UUID.randomUUID(), "Dev A2", companyA, "Company A", "Loc", "Desc", null, null, List.of(), List.of(), LocalDateTime.now());
    offer3 = new JobOffer(UUID.randomUUID(), "Dev B", companyB, "Company B", "Loc", "Desc", null, null, List.of(), List.of(), LocalDateTime.now());
  }

  @Test
  void shouldSortOffersByCompanyPopularityDescending() {
    List<JobOffer> unordered = List.of(offer3, offer1, offer2);
    List<JobOffer> sorted = rule.apply(unordered);

    assertEquals(3, sorted.size());
    assertTrue(sorted.indexOf(offer1) < sorted.indexOf(offer3));
    assertTrue(sorted.indexOf(offer2) < sorted.indexOf(offer3));
  }

  @Test
  void shouldReturnEmptyListIfNoOffers() {
    List<JobOffer> result = rule.apply(List.of());
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void shouldReturnSameListIfAllCompaniesEqual() {
    JobOffer sameCompany1 = new JobOffer(UUID.randomUUID(), "Title1", companyA, "Company A", "Loc", "Desc", null, null, List.of(), List.of(), LocalDateTime.now());
    JobOffer sameCompany2 = new JobOffer(UUID.randomUUID(), "Title2", companyA, "Company A", "Loc", "Desc", null, null, List.of(), List.of(), LocalDateTime.now());

    List<JobOffer> result = rule.apply(List.of(sameCompany1, sameCompany2));
    assertEquals(2, result.size());
    assertTrue(result.containsAll(List.of(sameCompany1, sameCompany2)));
  }
}

