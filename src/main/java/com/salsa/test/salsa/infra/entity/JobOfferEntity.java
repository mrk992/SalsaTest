package com.salsa.test.salsa.infra.entity;

import com.salsa.test.salsa.domain.model.JobType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job_offers")
@Data
public class JobOfferEntity {

  @Id
  private String id;

  private String jobTitle;
  private UUID companyId;
  private String companyName;
  private String location;

  @Column(length = 5000)
  private String description;

  @Enumerated(EnumType.STRING)
  private JobType jobType;

  private double minSalary;
  private double maxSalary;

  @ElementCollection
  private List<String> benefits;

  @ElementCollection
  private List<String> extras;

  private LocalDateTime createdAt;

}

