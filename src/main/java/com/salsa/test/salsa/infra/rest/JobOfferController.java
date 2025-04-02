package com.salsa.test.salsa.infra.rest;

import com.salsa.test.salsa.application.JobOfferService;
import com.salsa.test.salsa.domain.model.JobOffer;
import com.salsa.test.salsa.domain.model.SearchCriteria;
import com.salsa.test.salsa.infra.rest.dto.JobOfferRequest;
import com.salsa.test.salsa.infra.rest.dto.JobOfferResponse;
import com.salsa.test.salsa.infra.rest.mapper.JobOfferWebMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/job-offers")
public class JobOfferController {

  private final JobOfferService jobOfferService;
  private final JobOfferWebMapper webMapper;

  public JobOfferController(JobOfferService jobOfferService, JobOfferWebMapper webMapper) {
    this.jobOfferService = jobOfferService;
    this.webMapper = webMapper;
  }

  @GetMapping
  public Page<JobOfferResponse> searchJobOffers(
      @Valid SearchCriteria criteria,
      Pageable pageable
  ) {
    log.debug("[JobOfferController.searchJobOffers] - request to search for job with criteria: {}", criteria.toString());
    return jobOfferService.search(criteria, pageable)
        .map(webMapper::toResponse);
  }

  @GetMapping("/{id}")
  public ResponseEntity<JobOfferResponse> getById(@PathVariable UUID id) {
    log.debug("[JobOfferController.getById] - request to get job with id: {}", id);
    return jobOfferService.findById(id)
        .map(webMapper::toResponse)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<JobOfferResponse> create(@Valid @RequestBody JobOfferRequest request) {
    log.debug("[JobOfferController.create] - request to create a job with request: {}", request.toString());
    JobOffer offer = webMapper.toDomain(request);
    jobOfferService.create(offer);
    return ResponseEntity.ok(webMapper.toResponse(offer));
  }
}
