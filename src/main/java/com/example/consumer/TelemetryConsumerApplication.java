package com.example.consumer;

import com.example.consumer.config.MqttProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Spring Boot application entry point for the telemetry consumer service.
 * Subscribes to MQTT topics and processes IoT device telemetry data.
 */
@SpringBootApplication
@EnableConfigurationProperties(MqttProperties.class)
public class TelemetryConsumerApplication {

    /**
     * Main entry point for the application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(
                TelemetryConsumerApplication.class,
                args
        );
    }

}