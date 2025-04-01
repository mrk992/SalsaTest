package com.salsa.test.salsa.infra.persistence.mapper;

import com.salsa.test.salsa.domain.model.JobOffer;
import com.salsa.test.salsa.domain.model.SalaryRange;
import com.salsa.test.salsa.infra.entity.JobOfferEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JobOfferMapper {

  public JobOffer toDomain(JobOfferEntity entity) {
    return new JobOffer(
        UUID.fromString(entity.getId()),
        entity.getJobTitle(),
        entity.getCompanyId(),
        entity.getCompanyName(),
        entity.getLocation(),
        entity.getDescription(),
        entity.getJobType(),
        new SalaryRange(entity.getMinSalary(), entity.getMaxSalary()),
        entity.getBenefits(),
        entity.getExtras(),
        entity.getCreatedAt()
    );
  }

  public JobOfferEntity toEntity(JobOffer offer) {
    JobOfferEntity entity = new JobOfferEntity();
    entity.setId(String.valueOf(offer.id()));
    entity.setJobTitle(offer.jobTitle());
    entity.setCompanyId(offer.companyId());
    entity.setCompanyName(offer.companyName());
    entity.setLocation(offer.location());
    entity.setDescription(offer.description());
    entity.setJobType(offer.jobType());
    entity.setMinSalary(offer.salaryRange().min());
    entity.setMaxSalary(offer.salaryRange().max());
    entity.setBenefits(offer.benefits());
    entity.setExtras(offer.extras());
    entity.setCreatedAt(offer.createdAt());
    return entity;
  }
}
