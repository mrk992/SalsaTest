package com.salsa.test.salsa.domain.service;

import com.salsa.test.salsa.domain.model.JobOffer;
import com.salsa.test.salsa.domain.rules.OrderRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JobOfferRankingServiceTest {

  private OrderRule ruleA;
  private OrderRule ruleB;

  private JobOffer offer1;
  private JobOffer offer2;

  @BeforeEach
  void setUp() {
    ruleA = mock(OrderRule.class);
    ruleB = mock(OrderRule.class);

    offer1 = new JobOffer(
        UUID.randomUUID(), "A", UUID.randomUUID(), "Comp A", "Loc",
        "Desc", null, null, List.of(), List.of(), LocalDateTime.now()
    );
    offer2 = new JobOffer(
        UUID.randomUUID(), "B", UUID.randomUUID(), "Comp B", "Loc",
        "Desc", null, null, List.of(), List.of(), LocalDateTime.now()
    );

    Map<String, OrderRule> rulesMap = new HashMap<>();
    rulesMap.put("ruleA", ruleA);
    rulesMap.put("ruleB", ruleB);

  }

  @Test
  void shouldApplyRulesInOrder() {
    List<JobOffer> input = List.of(offer1, offer2);
    List<JobOffer> afterRuleA = List.of(offer2, offer1);
    List<JobOffer> afterRuleB = List.of(offer1, offer2);

    when(ruleA.apply(input)).thenReturn(afterRuleA);
    when(ruleB.apply(afterRuleA)).thenReturn(afterRuleB);

    JobOfferRankingService service = new JobOfferRankingService(List.of("ruleA", "ruleB"));
    List<JobOffer> result = service.rank(input);

    assertEquals(afterRuleB, result);
  }

  @Test
  void shouldSkipUnknownRules() {
    List<JobOffer> input = List.of(offer1, offer2);

    JobOfferRankingService service = new JobOfferRankingService(List.of("ruleC"));
    List<JobOffer> result = service.rank(input);

    assertEquals(input, result);
  }

  @Test
  void shouldHandleEmptyRuleList() {
    JobOfferRankingService service = new JobOfferRankingService(List.of());
    List<JobOffer> result = service.rank(List.of(offer1, offer2));

    assertEquals(List.of(offer1, offer2), result);
  }

  @Test
  void shouldReturnEmptyWhenNoOffers() {
    JobOfferRankingService service = new JobOfferRankingService(List.of("ruleA"));
    when(ruleA.apply(List.of())).thenReturn(List.of());

    List<JobOffer> result = service.rank(List.of());
    assertTrue(result.isEmpty());
  }
}
