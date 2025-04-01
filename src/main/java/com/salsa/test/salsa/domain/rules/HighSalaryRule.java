package com.salsa.test.salsa.domain.rules;

import com.salsa.test.salsa.domain.model.JobOffer;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component("salary")
public class HighSalaryRule implements OrderRule {

  private double extractMax(JobOffer offer) {
    return offer.salaryRange() != null ? offer.salaryRange().max() : 0.0;
  }

  @Override
  public List<JobOffer> apply(List<JobOffer> offers) {
    return offers.stream()
        .sorted(Comparator.comparing(this::extractMax).reversed())
        .toList();
  }
}
