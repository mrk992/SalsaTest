package com.salsa.test.salsa.infra.rest;

import com.salsa.test.salsa.application.JobOfferService;
import com.salsa.test.salsa.domain.model.JobOffer;
import com.salsa.test.salsa.domain.model.SearchCriteria;
import com.salsa.test.salsa.infra.rest.dto.JobOfferRequest;
import com.salsa.test.salsa.infra.rest.dto.JobOfferResponse;
import com.salsa.test.salsa.infra.rest.mapper.JobOfferWebMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    return jobOfferService.search(criteria, pageable)
        .map(webMapper::toResponse);
  }

  @GetMapping("/{id}")
  public ResponseEntity<JobOfferResponse> getById(@PathVariable UUID id) {
    return jobOfferService.findById(id)
        .map(webMapper::toResponse)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<JobOfferResponse> create(@Valid @RequestBody JobOfferRequest request) {
    JobOffer offer = webMapper.toDomain(request);
    jobOfferService.create(offer);
    return ResponseEntity.ok(webMapper.toResponse(offer));
  }
}
