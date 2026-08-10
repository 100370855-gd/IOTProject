package com.example.consumer.repository;

import com.example.consumer.model.TelemetryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for TelemetryEntity database operations.
 */
@Repository
public interface TelemetryRepository
        extends JpaRepository<TelemetryEntity, Long> {
}