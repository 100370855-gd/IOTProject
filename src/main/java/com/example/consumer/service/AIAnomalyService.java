package com.example.consumer.service;

import com.anthropic.client.AnthropicClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.anthropic.models.messages.TextBlock;
import com.example.consumer.model.Telemetry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AIAnomalyService {

    private final AnthropicClient claudeClient;
    private final ObjectMapper mapper;

    public AIAnomalyService(AnthropicClient claudeClient, ObjectMapper mapper) {
        this.claudeClient = claudeClient;
        this.mapper = mapper;
    }

    public String summarize(List<Telemetry> telemetry) {

        try {
            mapper.registerModule(new JavaTimeModule());
            String telemetryJson = mapper.writeValueAsString(telemetry);

            String prompt = """
                    Analyze the following IoT telemetry readings.

                    Provide:
                    1. A short summary of the device's recent behavior.
                    2. Any unusual trends.
                    3. Potential problems.
                    4. A recommendation if necessary.

                    Do not invent values that are not present in the data.

                    Telemetry:
                    """ + telemetryJson;

            // using CLAUDE
            MessageCreateParams params = MessageCreateParams.builder()
                    .model(Model.CLAUDE_SONNET_4_6)
                    .maxTokens(1024L)
                    .addUserMessage(prompt)
                    .build();

            Message response = claudeClient.messages().create(params);

            return response.content().stream()
                    .flatMap(content -> content.text().stream())
                    .map(TextBlock::text)
                    .collect(Collectors.joining());

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to analyze telemetry with AI", e
            );
        }
    }
}