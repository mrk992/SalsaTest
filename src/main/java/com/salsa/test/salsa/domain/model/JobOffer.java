package com.salsa.test.salsa.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record JobOffer(
    UUID id,
    String jobTitle,
    UUID companyId,
    String companyName,
    String location,
    String description,
    JobType jobType,
    SalaryRange salaryRange,
    List<String> benefits,
    List<String> extras,
    LocalDateTime createdAt
) {
}
