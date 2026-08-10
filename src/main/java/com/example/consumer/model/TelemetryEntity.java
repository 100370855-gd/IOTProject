package com.example.consumer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * JPA entity representing telemetry data persisted in the database.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "telemetry")
public class TelemetryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String deviceId;

    @Column(nullable = false)
    private double temperature;

    @Column(nullable = false)
    private double humidity;

    @Column(nullable = false)
    private int battery;

    @Column(nullable = false)
    private Instant timestamp;

    @Override
    public String toString() {
        return "TelemetryEntity{" +
                "id=" + id +
                ", deviceId='" + deviceId + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", battery=" + battery +
                ", timestamp=" + timestamp +
                '}';
    }
}