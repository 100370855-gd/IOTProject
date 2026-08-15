A Spring Boot application that consumes IoT telemetry messages over MQTT, persists them to a database, maintains a recent in-memory telemetry cache, detects threshold-based anomalies, and provides device health information through a REST API.
The project also includes an AI analysis layer that can use Claude to summarize recent telemetry data.

                    ┌─────────────────┐
                    │   IoT Sensors   │
                    └────────┬────────┘
                             │
                        MQTT messages
                             │
                             ▼
                    ┌─────────────────┐
                    │ MqttSubscriber  │
                    └────────┬────────┘
                             │
                         Telemetry
                             │
                             ▼
                    ┌─────────────────┐
                    │ TelemetryService│
                    └────┬─────┬──────┘
                         │     │
              ┌──────────┘     └──────────┐
              ▼                           ▼
       ┌──────────────┐            ┌──────────────┐
       │  PostgreSQL  │            │ In-Memory     │
       │   Database   │            │ Telemetry     │
       └──────────────┘            │ Cache         │
                                   └──────┬────────┘
                                          │
                              ┌───────────┴───────────┐
                              ▼                       ▼
                       Anomaly Checks            Claude AI
                       (thresholds)             (analysis)


### Features
- MQTT telemetry subscription
- JSON → Java object deserialization using Jackson
- Persistent telemetry storage using JPA/Hibernate
- In-memory cache of recent telemetry per device
- Threshold-based anomaly detection
- Device health score
- REST API for accessing device information
- AI-based analysis of recent telemetry using Claude
- Environment-variable based configuration for sensitive credentials

### REST API Endpoints 
 
| Method | Endpoint                                    | Description                                                                       |
| ------ | ------------------------------------------- | --------------------------------------------------------------------------------- |
| `GET`  | `/api/telemetry/devices/{deviceId}/summary` | Generates an AI summary of the device's recent cached telemetry.                  |
| `GET`  | `/api/telemetry/devices/{deviceId}/health`  | Returns the device health score based on battery, temperature, and online status. |


### Technology Stack
- Java 21+
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- MQTT
- Jackson
- Lombok
- PostgreSQL / relational database
- Anthropic Claude API
- Maven
  
