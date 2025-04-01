package com.salsa.test.salsa.domain.port;

import com.salsa.test.salsa.domain.model.JobOffer;
import com.salsa.test.salsa.domain.model.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobOfferPort {
  void save(JobOffer offer);

  Optional<JobOffer> findById(UUID id);

  List<JobOffer> findAll();

  void deleteById(UUID id);

  Page<JobOffer> search(SearchCriteria criteria, Pageable pageable);
}