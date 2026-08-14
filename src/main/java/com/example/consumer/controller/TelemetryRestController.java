package com.example.consumer.controller;

import com.example.consumer.model.Telemetry;
import com.example.consumer.repository.TelemetryRepository;
import com.example.consumer.service.AIAnomalyService;
import com.example.consumer.service.TelemetryService;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for telemetry operations.
 * Exposes endpoints to retrieve device health metrics.
 */
@RestController
@RequestMapping("/api/telemetry")
public class TelemetryRestController {

    private final TelemetryService service;
    private final AIAnomalyService aiAnomalyService;

    public TelemetryRestController(TelemetryService service, AIAnomalyService aiAnomalyService) {
        this.service = service;
        this.aiAnomalyService = aiAnomalyService;
    }

    /**
     * Retrieves the health score for a specific device.
     *
     * @param deviceId the device ID
     * @return health score (0-100)
     */
    @GetMapping("/devices/{deviceId}/health")
    public int getHealth(@PathVariable String deviceId) {
        return service.getDeviceHealthScore(deviceId);
    }

    /**
     * Retrieves the AI summary from the cached data of the specific device.
     * @param deviceId the device ID
     * @return summary.
     */
    @GetMapping("/devices/{deviceId}/summary")
    public String getSummary(@PathVariable String deviceId) {
        return aiAnomalyService.summarize(service.getCachedTelemetry(deviceId));
    }

}
