package com.salsa.test.salsa.application;

import com.salsa.test.salsa.domain.model.JobOffer;
import com.salsa.test.salsa.domain.model.SearchCriteria;
import com.salsa.test.salsa.domain.port.JobOfferPort;
import com.salsa.test.salsa.domain.service.JobOfferRankingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobOfferService {

  private final JobOfferPort jobOfferPort;
  private final JobOfferRankingService rankingService;

  public JobOfferService(JobOfferPort jobOfferPort, JobOfferRankingService rankingService) {
    this.jobOfferPort = jobOfferPort;
    this.rankingService = rankingService;
  }

  public void create(JobOffer offer) {
    jobOfferPort.save(offer);
  }

  public Optional<JobOffer> findById(UUID id) {
    return jobOfferPort.findById(id);
  }

  public List<JobOffer> findAll() {
    List<JobOffer> offers = jobOfferPort.findAll();
    return rankingService.rank(offers);
  }

  public void delete(UUID id) {
    jobOfferPort.deleteById(id);
  }

  public Page<JobOffer> search(SearchCriteria criteria, Pageable pageable) {
    Page<JobOffer> offers = jobOfferPort.search(criteria, pageable);
    List<JobOffer> ranked = rankingService.rank(offers.getContent());
    return new PageImpl<>(ranked, pageable, offers.getTotalElements());
  }
}
