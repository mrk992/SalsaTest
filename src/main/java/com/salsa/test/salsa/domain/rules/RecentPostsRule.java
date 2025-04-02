package com.salsa.test.salsa.domain.rules;

import com.salsa.test.salsa.domain.model.JobOffer;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Component("recent")
public class RecentPostsRule implements OrderRule {

  private static final int DAYS_THRESHOLD = 7;
  private final Clock clock;

  public RecentPostsRule() {
    this(Clock.systemDefaultZone());
  }

  public RecentPostsRule(Clock clock) {
    this.clock = clock;
  }

  @Override
  public List<JobOffer> apply(List<JobOffer> offers) {
    LocalDateTime now = LocalDateTime.now(clock);

    return offers.stream()
        .sorted(Comparator
            .comparing((JobOffer offer) -> offer.createdAt().isAfter(now.minusDays(DAYS_THRESHOLD)) ? 0 : 1)
            .thenComparing(JobOffer::createdAt, Comparator.reverseOrder())
        )
        .toList();
  }
}
