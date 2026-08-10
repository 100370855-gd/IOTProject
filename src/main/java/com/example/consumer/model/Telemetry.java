package com.example.consumer.model;

import java.time.Instant;

/**
 * Record representing telemetry data from IoT devices.
 */
public record Telemetry(
        String deviceId,
        double temperature,
        double humidity,
        int battery,
        Instant timestamp
) {}