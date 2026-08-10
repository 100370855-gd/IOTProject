package com.example.consumer.service;

import com.example.consumer.model.Telemetry;
import com.example.consumer.repository.TelemetryRepository;
import com.example.consumer.model.TelemetryEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

@Service
public class TelemetryService {

    private static final long MAX_AGE_SECONDS = 120L;
    private static final int MIN_BATTERY = 10;
    private static final int MAX_TEMPERATURE = 85;
    private static final int CACHE_SIZE = 10;

    private final TelemetryRepository repository;
    private final Map<String, Queue<Telemetry>> telemetryCache = new ConcurrentHashMap<>();

    public TelemetryService(TelemetryRepository repository) {
        this.repository = repository;
    }

    /**
     * Saves telemetry data to the repository and cache, then detects anomalies.
     * @param telemetry the telemetry data to save
     */
    public void save(Telemetry telemetry) {

        TelemetryEntity entity = new TelemetryEntity();

        entity.setDeviceId(telemetry.deviceId());
        entity.setTemperature(telemetry.temperature());
        entity.setHumidity(telemetry.humidity());
        entity.setBattery(telemetry.battery());
        entity.setTimestamp(telemetry.timestamp());

        repository.save(entity);
        System.out.println("Saved telemetry for " + telemetry.deviceId());
        
        addToCache(telemetry);
        detectSingularAnomalies(telemetry);
    }

    /**
    * Detects various anomalies in the telemetry data (overheating, low battery, offline).
    * @param telemetry the telemetry data to check for anomalies
    */
    private void detectSingularAnomalies(Telemetry telemetry) {
        isOverHeated(telemetry);
        isLowBattery(telemetry);
        isOffline(telemetry);
    }

    /**
     * Adds telemetry to the in-memory cache, maintaining last 10 records per device.
     * @param telemetry the telemetry data to cache
     */
    private void addToCache(Telemetry telemetry) {
        telemetryCache.computeIfAbsent(telemetry.deviceId(), k -> new LinkedList<>())
                .offer(telemetry);
        
        Queue<Telemetry> queue = telemetryCache.get(telemetry.deviceId());
        if (queue.size() > CACHE_SIZE) {
            queue.poll();
        }
        System.out.println("Cached telemetry for " + telemetry.deviceId() + ": " + queue);
    }

    /**
     * Detects if device temperature is overheating.
     * @param telemetry the telemetry data to check
     * @return true if temperature &gt; MAX_TEMPERATURE, false otherwise
     */
    private boolean isOverHeated(Telemetry telemetry) {
        if (telemetry.temperature() > MAX_TEMPERATURE) {
            System.out.println("Overheating detected in device " + telemetry.deviceId()
                    + " at " + telemetry.timestamp());
            return true;
        }
        return false;
    }

    /**
     * Detects if device battery level is low.
     * @param telemetry the telemetry data to check
     * @return true if battery &lt; MIN_BATTERY, false otherwise
     */
    private boolean isLowBattery(Telemetry telemetry) {
        if (telemetry.battery() < MIN_BATTERY) {
            System.out.println("Battery is low in device " + telemetry.deviceId()
                    + " at " + telemetry.timestamp());
            return true;
        }
        return false;
    }

    /**
     * Detects if device is offline (no data received within MAX_AGE_SECONDS).
     * @param telemetry the telemetry data to check
     * @return true if telemetry is older than MAX_AGE_SECONDS, false otherwise
     */
    private boolean isOffline(Telemetry telemetry) {
        long age = Duration.between(telemetry.timestamp(), Instant.now()).toSeconds();
        if (age > MAX_AGE_SECONDS) {
            System.out.println(telemetry.deviceId() + " hasn't responded for " + MAX_AGE_SECONDS + " seconds. Device is Offline");
            return true;
        }
        return false;
    }

    /**
     * Computes a 0–100 health score from a telemetry sample.
     * Penalizes low battery, overheating, and offline status.
     *
     * @param deviceId deviceId sample
     * @return health score (0-100)
     */
    private int calculateDeviceHealthScore(String deviceId) {
        int score = 100;
        Telemetry telemetry = getCachedTelemetry(deviceId).getLast();

        if (isLowBattery(telemetry)) {
            score -= 30;
        }
        if (isOverHeated(telemetry)) {
            score -= 30;
        }
        if (isOffline(telemetry)) {
            score -= 40;
        }
        return Math.max(score, 0);
    }

    /**
     * Retrieves the computed health score for a device.
     * Delegates to calculateDeviceHealthScore using the most recent cached telemetry.
     *
     * @param deviceId the device ID
     * @return health score (0-100)
     */
    public int getDeviceHealthScore(String deviceId) {
        return calculateDeviceHealthScore(deviceId);
    }

    /**
     * Retrieves cached telemetry for a specific device.
     * @param deviceId the device ID
     * @return list of last 10 telemetry records for the device, or empty list if no data
     */
    public List<Telemetry> getCachedTelemetry(String deviceId) {
        Queue<Telemetry> queue = telemetryCache.get(deviceId);
        return queue != null ? new ArrayList<>(queue) : Collections.emptyList();
    }

//    /**
//     * Retrieves all cached telemetry for all devices.
//     * @return map of device IDs to lists of cached telemetry
//     */
//    public Map<String, List<Telemetry>> getAllCachedTelemetry() {
//        Map<String, List<Telemetry>> result = new ConcurrentHashMap<>();
//        telemetryCache.forEach((deviceId, queue) ->
//                result.put(deviceId, new ArrayList<>(queue))
//        );
//        return result;
//    }
}