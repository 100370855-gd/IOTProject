package com.example.consumer.model;

import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;

import java.time.Instant;

/**
 * JPA entity representing telemetry data persisted in the database.
 */
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

    public TelemetryEntity() {
    }

    /**
     * Gets the primary key ID of the telemetry entity.
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

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