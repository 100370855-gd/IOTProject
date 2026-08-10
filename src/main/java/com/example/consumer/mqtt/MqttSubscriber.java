package com.example.consumer.mqtt;

import com.example.consumer.config.MqttProperties;
import com.example.consumer.model.Telemetry;
import com.example.consumer.service.TelemetryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * MQTT subscriber component that listens to IoT device telemetry messages and processes them.
 * Connects to MQTT broker on initialization and subscribes to configured topics.
 */
@Component
public class MqttSubscriber {

    private final Mqtt5AsyncClient client;
    private final TelemetryService service;
    private final MqttProperties properties;

    private final ObjectMapper mapper =
            new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * Initializes MqttSubscriber with required dependencies.
     * @param client MQTT async client
     * @param service telemetry service for processing data
     * @param properties MQTT configuration properties
     */
    public MqttSubscriber(Mqtt5AsyncClient client, TelemetryService service, MqttProperties properties) {
        this.client = client;
        this.service = service;
        this.properties = properties;
    }

    /**
     * Starts MQTT connection and subscribes to telemetry topics.
     * Called automatically after bean construction.
     */
    @PostConstruct
    public void start() {

        client.connectWith()
                .simpleAuth()
                .username(properties.getUsername())
                .password(properties.getPassword().getBytes())
                .applySimpleAuth()
                .send()
                .join();

        client.subscribeWith()
                .topicFilter(properties.getTopic()+"#")
                .callback(message -> {
                    try {
                        String json = new String(message.getPayloadAsBytes());
                        Telemetry telemetry = mapper.readValue(json,Telemetry.class);

                        System.out.println("Received: " + telemetry);

                        service.process(telemetry);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                })
                .send();

    }

}
