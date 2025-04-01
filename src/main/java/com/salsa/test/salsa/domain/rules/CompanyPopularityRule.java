package com.salsa.test.salsa.domain.rules;

import com.salsa.test.salsa.domain.model.JobOffer;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component("company")
public class CompanyPopularityRule implements OrderRule {

  @Override
  public List<JobOffer> apply(List<JobOffer> offers) {

    Map<UUID, Long> companyCounts = offers.stream()
        .collect(Collectors.groupingBy(JobOffer::companyId, Collectors.counting()));

    return offers.stream()
        .sorted(Comparator.comparing(
            (JobOffer offer) -> companyCounts.getOrDefault(offer.companyId(), 0L)
        ).reversed())
        .toList();
  }
}

