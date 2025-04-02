package com.salsa.test.salsa.domain.rules;

import com.salsa.test.salsa.domain.model.JobOffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecentPostsRuleTest {

  private RecentPostsRule rule;
  private Clock fixedClock;

  private JobOffer recentOffer;
  private JobOffer oldOffer;
  private JobOffer edgeCaseOffer;

  @BeforeEach
  void setUp() {
    LocalDateTime fixedNow = LocalDateTime.of(2025, 4, 1, 12, 0);
    fixedClock = Clock.fixed(fixedNow.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    rule = new RecentPostsRule(fixedClock);

    recentOffer = new JobOffer(
        UUID.randomUUID(), "Recent", UUID.randomUUID(), "Company A", "Loc", "Desc",
        null, null, List.of(), List.of(), fixedNow.minusDays(2)
    );

    oldOffer = new JobOffer(
        UUID.randomUUID(), "Old", UUID.randomUUID(), "Company B", "Loc", "Desc",
        null, null, List.of(), List.of(), fixedNow.minusDays(10)
    );

    edgeCaseOffer = new JobOffer(
        UUID.randomUUID(), "Edge", UUID.randomUUID(), "Company C", "Loc", "Desc",
        null, null, List.of(), List.of(), fixedNow.minusDays(7).plusSeconds(1)
    );
  }

  @Test
  void shouldSortRecentPostsFirst() {
    List<JobOffer> result = rule.apply(List.of(oldOffer, recentOffer, edgeCaseOffer));

    assertEquals(recentOffer, result.get(0));
    assertEquals(edgeCaseOffer, result.get(1));
    assertEquals(oldOffer, result.get(2));
  }

  @Test
  void shouldHandleEmptyList() {
    List<JobOffer> result = rule.apply(List.of());
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
