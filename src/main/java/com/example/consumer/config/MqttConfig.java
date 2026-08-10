package com.example.consumer.config;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class for MQTT client bean creation.
 */
@Configuration
public class MqttConfig {

    /**
     * Creates an MQTT 5.0 async client bean with SSL connection.
     * @param properties MQTT configuration properties
     * @return configured Mqtt5AsyncClient
     */
    @Bean
    public Mqtt5AsyncClient mqttClient(MqttProperties properties) {

        return MqttClient.builder()
                .useMqttVersion5()
                .sslWithDefaultConfig()
                .serverHost(properties.getHost())
                .serverPort(properties.getPort())
                .buildAsync();
    }
}
