package com.salsa.test.salsa.infra.persistence.adapter;

import com.salsa.test.salsa.domain.model.JobOffer;
import com.salsa.test.salsa.domain.model.SearchCriteria;
import com.salsa.test.salsa.domain.port.JobOfferPort;
import com.salsa.test.salsa.infra.entity.JobOfferEntity;
import com.salsa.test.salsa.infra.persistence.mapper.JobOfferMapper;
import com.salsa.test.salsa.infra.persistence.repository.JobOfferRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JobOfferAdapter implements JobOfferPort {

  private final JobOfferRepository repository;
  private final JobOfferMapper mapper;

  public JobOfferAdapter(JobOfferRepository repository, JobOfferMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public void save(JobOffer offer) {
    repository.save(mapper.toEntity(offer));
  }

  @Override
  public Optional<JobOffer> findById(UUID id) {
    return repository.findById(String.valueOf(id))
        .map(mapper::toDomain);
  }

  @Override
  public List<JobOffer> findAll() {
    return repository.findAll().stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public void deleteById(UUID id) {
    repository.deleteById(id.toString());
  }

  @Override
  public Page<JobOffer> search(SearchCriteria criteria, Pageable pageable) {
    Specification<JobOfferEntity> spec = (root, query, cb) -> {
      var predicates = cb.conjunction();

      if (criteria.title() != null && !criteria.title().isBlank()) {
        predicates = cb.and(predicates,
            cb.like(cb.lower(root.get("jobTitle")), "%" + criteria.title().toLowerCase() + "%"));
      }

      if (criteria.location() != null && !criteria.location().isBlank()) {
        predicates = cb.and(predicates,
            cb.like(cb.lower(root.get("location")), "%" + criteria.location().toLowerCase() + "%"));
      }

      if (criteria.salary() != null) {
        predicates = cb.and(predicates,
            cb.greaterThanOrEqualTo(root.get("maxSalary"), criteria.salary()));
      }

      return predicates;
    };

    return repository.findAll(spec, pageable).map(mapper::toDomain);
  }
}