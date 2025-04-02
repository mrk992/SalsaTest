package com.salsa.test.salsa.infra.persistence.adapter;

import com.salsa.test.salsa.domain.model.JobOffer;
import com.salsa.test.salsa.domain.model.SearchCriteria;
import com.salsa.test.salsa.domain.port.JobOfferPort;
import com.salsa.test.salsa.infra.entity.JobOfferEntity;
import com.salsa.test.salsa.infra.persistence.mapper.JobOfferMapper;
import com.salsa.test.salsa.infra.persistence.repository.JobOfferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Slf4j
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
  public Page<JobOffer> search(SearchCriteria criteria, Pageable pageable) {
    Specification<JobOfferEntity> specification = (root, query, criteriaBuilder) -> {
      var predicates = criteriaBuilder.conjunction();

      if (criteria.title() != null && !criteria.title().isBlank()) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.like(criteriaBuilder.lower(root.get("jobTitle")), "%" + criteria.title().toLowerCase() + "%"));
      }

      if (criteria.location() != null && !criteria.location().isBlank()) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), "%" + criteria.location().toLowerCase() + "%"));
      }

      if (criteria.salary() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.greaterThanOrEqualTo(root.get("maxSalary"), criteria.salary()));
      }

      return predicates;
    };
    log.debug("[JobOfferAdapter.search] - request to search in db with this specification {}", specification);
    return repository.findAll(specification, pageable).map(mapper::toDomain);
  }
}