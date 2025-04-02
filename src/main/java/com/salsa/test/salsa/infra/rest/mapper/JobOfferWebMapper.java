package com.salsa.test.salsa.infra.rest.mapper;

import com.salsa.test.salsa.domain.model.JobOffer;
import com.salsa.test.salsa.domain.model.SalaryRange;
import com.salsa.test.salsa.infra.rest.dto.JobOfferRequest;
import com.salsa.test.salsa.infra.rest.dto.JobOfferResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class JobOfferWebMapper {


  public JobOffer toDomain(JobOfferRequest request) {
    return new JobOffer(
        UUID.randomUUID(),
        request.jobTitle(),
        request.companyId(),
        request.companyName(),
        request.location(),
        request.description(),
        request.jobType(),
        new SalaryRange(request.minSalary(), request.maxSalary()),
        request.benefits(),
        request.extras(),
        LocalDateTime.now()
    );
  }

  public JobOfferResponse toResponse(JobOffer jobOffer) {
    SalaryRange salary = jobOffer.salaryRange();

    return new JobOfferResponse(
        jobOffer.id(),
        jobOffer.jobTitle(),
        jobOffer.companyId(),
        jobOffer.companyName(),
        jobOffer.location(),
        jobOffer.description(),
        jobOffer.jobType(),
        salary != null ? salary.min() : 0.0,
        salary != null ? salary.max() : 0.0,
        jobOffer.benefits(),
        jobOffer.extras(),
        jobOffer.createdAt()
    );
  }
}
