package com.salsa.test.salsa.infra.rest.mapper;

import com.salsa.test.salsa.domain.model.JobOffer;
import com.salsa.test.salsa.infra.rest.dto.JobOfferRequest;
import com.salsa.test.salsa.infra.rest.dto.JobOfferResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobOfferWebMapper {

  // Domain → Response
  @Mapping(target = "minSalary", source = "salaryRange.min")
  @Mapping(target = "maxSalary", source = "salaryRange.max")
  JobOfferResponse toResponse(JobOffer offer);

  // Request → Domain
  @Mapping(target = "id", expression = "java(UUID.randomUUID())")
  @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
  @Mapping(target = "salaryRange", expression = "java(new SalaryRange(request.minSalary(), request.maxSalary()))")
  JobOffer toDomain(JobOfferRequest request);
}
