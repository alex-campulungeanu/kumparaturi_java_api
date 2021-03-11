package com.alex.kumparaturi.repository;

import com.alex.kumparaturi.model.JobsConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobsConfigRepository extends JpaRepository<JobsConfig, Long> {
    Optional<JobsConfig> findByJobName(String jobName);
}
