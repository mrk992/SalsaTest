package com.salsa.test.salsa.infra.rest.dto;

import com.salsa.test.salsa.domain.model.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.UUID;

public record JobOfferRequest(

    @NotBlank(message = "Job title is required")
    String jobTitle,

    @NotNull(message = "Company ID is required")
    UUID companyId,

    @NotBlank(message = "Company name is required")
    String companyName,

    @NotBlank(message = "Location is required")
    String location,

    @NotBlank(message = "Description is required")
    String description,

    @NotNull(message = "Job type is required")
    JobType jobType,

    @Positive(message = "Minimum salary must be positive")
    double minSalary,

    @Positive(message = "Maximum salary must be positive")
    double maxSalary,

    List<@NotBlank(message = "Benefit cannot be blank") String> benefits,

    List<@NotBlank(message = "Extra cannot be blank") String> extras
) {
}
