package com.salsa.test.salsa.domain.rules;

import com.salsa.test.salsa.domain.model.JobOffer;

import java.util.List;

public interface OrderRule {
  List<JobOffer> apply(List<JobOffer> offers);
}