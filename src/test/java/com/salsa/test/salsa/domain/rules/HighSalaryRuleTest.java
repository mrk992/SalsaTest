package com.salsa.test.salsa.domain.rules;

import com.salsa.test.salsa.domain.model.JobOffer;
import com.salsa.test.salsa.domain.model.SalaryRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HighSalaryRuleTest {

  private HighSalaryRule rule;

  private JobOffer offerLow;
  private JobOffer offerHigh;
  private JobOffer offerNoSalary;

  @BeforeEach
  void setUp() {
    rule = new HighSalaryRule();

    offerLow = new JobOffer(
        UUID.randomUUID(), "Junior Dev", UUID.randomUUID(), "Company X", "Location",
        "Low salary", null, new SalaryRange(10000, 20000),
        List.of(), List.of(), LocalDateTime.now()
    );

    offerHigh = new JobOffer(
        UUID.randomUUID(), "Senior Dev", UUID.randomUUID(), "Company Y", "Location",
        "High salary", null, new SalaryRange(50000, 80000),
        List.of(), List.of(), LocalDateTime.now()
    );

    offerNoSalary = new JobOffer(
        UUID.randomUUID(), "Unknown", UUID.randomUUID(), "Company Z", "Location",
        "No salary info", null, null,
        List.of(), List.of(), LocalDateTime.now()
    );
  }

  @Test
  void shouldSortByMaxSalaryDescending() {
    List<JobOffer> unordered = List.of(offerLow, offerHigh);
    List<JobOffer> result = rule.apply(unordered);

    assertEquals(2, result.size());
    assertEquals(offerHigh, result.get(0));
    assertEquals(offerLow, result.get(1));
  }

  @Test
  void shouldTreatNullSalaryAsZero() {
    List<JobOffer> offers = List.of(offerHigh, offerNoSalary, offerLow);
    List<JobOffer> result = rule.apply(offers);

    assertEquals(offerHigh, result.get(0));
    assertEquals(offerLow, result.get(1));
    assertEquals(offerNoSalary, result.get(2)); // debe ir al final
  }

  @Test
  void shouldReturnEmptyListIfNoOffers() {
    List<JobOffer> result = rule.apply(List.of());
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
