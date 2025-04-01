package com.salsa.test.salsa.infra.persistence.repository;


import com.salsa.test.salsa.infra.entity.JobOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobOfferRepository extends JpaRepository<JobOfferEntity, String>,
    JpaSpecificationExecutor<JobOfferEntity> {
}