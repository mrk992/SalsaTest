package com.salsa.test.salsa.application;

import com.salsa.test.salsa.domain.model.JobOffer;
import com.salsa.test.salsa.domain.model.SearchCriteria;
import com.salsa.test.salsa.domain.port.JobOfferPort;
import com.salsa.test.salsa.domain.service.JobOfferRankingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class JobOfferService {

  private final JobOfferPort jobOfferPort;
  private final JobOfferRankingService rankingService;

  public JobOfferService(JobOfferPort jobOfferPort, JobOfferRankingService rankingService) {
    this.jobOfferPort = jobOfferPort;
    this.rankingService = rankingService;
  }

  public void create(JobOffer offer) {
    log.debug("[JobOfferService.getById] - request to create job: {}", offer);
    jobOfferPort.save(offer);
  }

  public Optional<JobOffer> findById(UUID id) {
    log.debug("[JobOfferService.getById] - request to get job with id: {}", id);
    return jobOfferPort.findById(id);
  }

  public Page<JobOffer> search(SearchCriteria criteria, Pageable pageable) {

    Page<JobOffer> offers = jobOfferPort.search(criteria, pageable);
    log.debug("[JobOfferService.search] - founded: {} jobs for criteria: {}", offers.getTotalElements(), criteria.toString());

    List<JobOffer> ranked = rankingService.rank(offers.getContent());
    log.debug("[JobOfferService.search] - offers sorted: {}", ranked);

    return new PageImpl<>(ranked, pageable, offers.getTotalElements());
  }
}
