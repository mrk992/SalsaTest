package com.salsa.test.salsa.infra.rest.dto;

import com.salsa.test.salsa.domain.model.JobType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record JobOfferResponse(
    UUID id,
    String jobTitle,
    UUID companyId,
    String companyName,
    String location,
    String description,
    JobType jobType,
    double minSalary,
    double maxSalary,
    List<String> benefits,
    List<String> extras,
    LocalDateTime createdAt
) {
}
