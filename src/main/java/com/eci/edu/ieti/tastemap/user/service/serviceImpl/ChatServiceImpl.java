package com.eci.edu.ieti.tastemap.user.service.serviceImpl;

import com.eci.edu.ieti.tastemap.user.dto.ChatRequestDto;
import com.eci.edu.ieti.tastemap.user.dto.ChatResponseDto;
import com.eci.edu.ieti.tastemap.user.service.ChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService {

    private final WebClient webClient;
    private final String webhookUrl;

    public ChatServiceImpl(WebClient webClient, @Value("${n8n.webhook.url}") String webhookUrl) {
        this.webClient = webClient;
        this.webhookUrl = webhookUrl;
    }

    @Override
    public ChatResponseDto sendMessage(String userId, ChatRequestDto request) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("userId", userId);
        payload.put("message", request.getMessage());
        payload.put("conversationHistory", request.getConversationHistory());

        ChatResponseDto response = webClient.post()
                .uri(webhookUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(payload))
                .retrieve()
                .bodyToMono(ChatResponseDto.class)
                .block();

        if (response == null) {
            return ChatResponseDto.builder()
                    .success(false)
                    .response("No response returned by webhook")
                    .userId(userId)
                    .build();
        }

        response.setUserId(userId);
        return response;
    }
}