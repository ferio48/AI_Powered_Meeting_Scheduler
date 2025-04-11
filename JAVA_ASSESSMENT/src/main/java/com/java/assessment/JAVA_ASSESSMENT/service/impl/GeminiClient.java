package com.java.assessment.JAVA_ASSESSMENT.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.java.assessment.JAVA_ASSESSMENT.model.dto.MeetingInformationDto;
import com.java.assessment.JAVA_ASSESSMENT.model.entity.ConversationResult;
import com.java.assessment.JAVA_ASSESSMENT.model.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GeminiClient {

    @Value("${gemini.api.url}")
    private String geminiAPIUrl;

    @Value("${gemini.api.key}")
    private String geminiAPIKey;

    private static final ObjectMapper mapper = new ObjectMapper();

    public ConversationResult handleConversation(List<Message> history) throws Exception {
        final String finalGeminiURL = geminiAPIUrl + geminiAPIKey;
        HttpClient client = HttpClient.newHttpClient();

        ArrayNode contents = mapper.createArrayNode();
        for (Message message : history) {
            ObjectNode part = mapper.createObjectNode();
            part.put("text", message.getText());

            ObjectNode content = mapper.createObjectNode();
            content.put("role", message.getRole());

            ArrayNode parts = mapper.createArrayNode();
            parts.add(part);
            content.set("parts", parts);

            contents.add(content);
        }

        ObjectNode body = mapper.createObjectNode();
        body.set("contents", contents);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(finalGeminiURL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectNode root = (ObjectNode) mapper.readTree(response.body());
        ArrayNode candidates = (ArrayNode) root.get("candidates");

        if (candidates != null && !candidates.isEmpty()) {
            String geminiResponse = candidates.get(0)
                    .get("content")
                    .get("parts")
                    .get(0)
                    .get("text")
                    .asText()
                    .trim();

            if (geminiResponse.startsWith("```json")) {
                geminiResponse = geminiResponse
                        .replace("```json", "")
                        .replace("```", "")
                        .trim();
            }

            if (geminiResponse.startsWith("{") && geminiResponse.endsWith("}")) {
                try {
                    MeetingInformationDto dto = mapper.readValue(geminiResponse, MeetingInformationDto.class);
                    return ConversationResult.fromJson(dto);
                } catch (Exception e) {
                    return ConversationResult.fromQuestion("Gemini returned invalid JSON. Please try again.");
                }
            } else {
                return ConversationResult.fromQuestion(geminiResponse);
            }
        }

        throw new RuntimeException("No valid response from Gemini.");
    }

}
