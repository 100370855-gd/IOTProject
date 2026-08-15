package com.example.simulator;

import com.example.consumer.model.Telemetry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;


/**
 * Standalone application that simulates IoT sensor devices.
 * Generates telemetry data from multiple sensors and publishes to MQTT broker.
 */
public class SensorSimulatorApplication {

    static Properties properties = new Properties();

    static {
        try (InputStream input =
                SensorSimulatorApplication.class
                        .getClassLoader()
                        .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException(
                        "application.properties not found"
                );
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Main entry point for the sensor simulator.
     * Connects to MQTT broker and continuously publishes telemetry from 5 simulated sensors.
     * @param args command line arguments
     * @throws Exception if MQTT connection or publishing fails
     */
    public static void main(String[] args) throws Exception {

        Mqtt5AsyncClient client =
                MqttClient.builder()
                        .useMqttVersion5()
                        .sslWithDefaultConfig()
                        .serverHost(getProperty("mqtt.host", "MQTT_HOST"))
                        .serverPort(Integer.parseInt(getProperty("mqtt.port", "MQTT_PORT")))
                        .buildAsync();


        client.connectWith()
                .simpleAuth()
                .username(getProperty("mqtt.username", "MQTT_USERNAME"))
                .password(getProperty("mqtt.password", "MQTT_PASSWORD").getBytes(StandardCharsets.UTF_8))
                .applySimpleAuth()
                .send()
                // add a listener
                /*.whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                    } else {
                        System.out.println("Connected!");
                        System.out.println(connAck);
                    }
                })*/
                .join();

        System.out.println("Connected to MQTT broker");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // 5 IOT devices
        List<Sensor> sensors = List.of(
                new Sensor("sensor-01"),
                new Sensor("sensor-02"),
                new Sensor("sensor-03"),
                new Sensor("sensor-04"),
                new Sensor("sensor-05")
        );

        while (true) {
            for (Sensor sensor : sensors) {

                Telemetry telemetry = sensor.generateTelemetry();

                String json = mapper.writeValueAsString(telemetry);

                client.publishWith()
                        .topic(getProperty("mqtt.topic", "") + telemetry.deviceId())
                        .payload(json.getBytes(StandardCharsets.UTF_8))
                        .send();

                System.out.println("Published: " + json);
            }

            Thread.sleep(3000);
        }
    }

    /**
     * Retrieves a property value with environment variable fallback.
     * Checks for the environment variable first; if not set or blank, falls back to the property file.
     * @param name the property name in the application.properties file
     * @param envName the environment variable name to check
     * @return the environment variable value if set and non-blank, otherwise the property file value
     */
    private static String getProperty(String name, String envName) {
        String envValue = System.getenv(envName);

        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        return properties.getProperty(name);
    }
}