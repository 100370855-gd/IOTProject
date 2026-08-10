package com.example.consumer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * Configuration properties for MQTT connection and topics.
 * Binds to properties prefixed with "mqtt" from application.yml/properties.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {

    private String host;
    private int port;
    private String username;
    private String password;
    private String topic;
}