package com.example.simulator;

import com.example.consumer.model.Telemetry;
import java.time.Instant;
import java.util.Random;

/**
 * Simulates an IoT sensor device that generates telemetry data.
 * Tracks temperature, humidity, and battery level with realistic variations.
 */
public class Sensor {

    private final String id;
    private final Random random = new Random();

    private double temperature = 22;
    private double humidity = 50;
    private int battery = 100;

    /**
     * Initializes a sensor with the given device ID.
     * @param id the unique sensor device ID
     */
    public Sensor(String id) {
        this.id = id;
    }

    /**
     * Generates telemetry data for this sensor.
     * Simulates realistic variations in temperature and humidity, battery depletion, and occasional anomalies.
     * @return Telemetry object with current sensor readings
     */
    public Telemetry generateTelemetry() {

        // simulate small changes
        temperature += random.nextDouble(-1, 1);
        humidity += random.nextDouble(-2, 2);

        // battery slowly decreases
        battery -= random.nextInt(0, 2);

        // occasionally create an anomaly
        if (random.nextInt(100) < 5) {
            temperature = 80 + random.nextDouble(10);
        }

        return new Telemetry(
                id,
                round(temperature),
                round(humidity),
                battery,
                Instant.now()
        );
    }

    /**
     * Rounds a numeric value to 2 decimal places.
     * @param value the value to round
     * @return rounded value
     */
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}