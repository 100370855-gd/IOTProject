package com.example.consumer.controller;

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

    public TelemetryRestController(TelemetryService service) {
        this.service = service;
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

}
