package com.aipowered.meeting.scheduler.service.impl;

import com.aipowered.meeting.scheduler.model.entity.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.aipowered.meeting.scheduler.model.dto.MeetingInformationDto;
import com.aipowered.meeting.scheduler.model.entity.ConversationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Service to interact with Google's Gemini API.
 * It handles sending structured conversation history to Gemini
 * and parsing its response into either a structured DTO or follow-up prompt.
 */
@Service
@RequiredArgsConstructor
public class GeminiClient {

    @Value("${gemini.api.url}")
    private String geminiAPIUrl;

    @Value("${gemini.api.key}")
    private String geminiAPIKey;

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Main handler for a chat conversation with Gemini.
     * @param history Full conversation history (user and assistant messages).
     * @return A ConversationResult containing either the final parsed appointment,
     *         or a follow-up question if required fields are missing.
     */
    public ConversationResult handleConversation(List<Message> history) throws Exception {
        String responseText = getGeminiResponseText(history);

        String cleanedResponse = sanitizeGeminiResponse(responseText);

        if (isJson(cleanedResponse)) {
            return parseJsonResponse(cleanedResponse);
        } else {
            return ConversationResult.fromQuestion(cleanedResponse);
        }
    }

    /**
     * Sends the message history to Gemini API and retrieves its response text.
     */
    private String getGeminiResponseText(List<Message> history) throws Exception {
        String finalGeminiURL = geminiAPIUrl + geminiAPIKey;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(finalGeminiURL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(prepareRequestBody(history)))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectNode root = (ObjectNode) mapper.readTree(response.body());
        ArrayNode candidates = (ArrayNode) root.get("candidates");

        if (candidates != null && !candidates.isEmpty()) {
            return candidates.get(0)
                    .get("content")
                    .get("parts")
                    .get(0)
                    .get("text")
                    .asText()
                    .trim();
        }

        throw new RuntimeException("No valid response from Gemini.");
    }

    /**
     * Builds the structured JSON payload to send to Gemini API.
     */
    private String prepareRequestBody(List<Message> history) throws Exception {
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

        return mapper.writeValueAsString(body);
    }

    /**
     * Removes Markdown formatting from Gemini's JSON response if wrapped in ```json blocks.
     */
    private String sanitizeGeminiResponse(String responseText) {
        if (responseText.startsWith("```json")) {
            return responseText
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();
        }
        return responseText;
    }

    /**
     * Checks whether a given string is a valid JSON object based on its format.
     */
    private boolean isJson(String text) {
        return text.startsWith("{") && text.endsWith("}");
    }

    /**
     * Attempts to deserialize Gemini's response text into MeetingInformationDto.
     */
    private ConversationResult parseJsonResponse(String json) {
        try {
            MeetingInformationDto dto = mapper.readValue(json, MeetingInformationDto.class);
            return ConversationResult.fromJson(dto);
        } catch (Exception e) {
            return ConversationResult.fromQuestion("Gemini returned invalid JSON. Please try again.");
        }
    }
}
