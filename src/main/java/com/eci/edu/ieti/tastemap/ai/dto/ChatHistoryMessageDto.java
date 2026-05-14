package com.eci.edu.ieti.tastemap.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistoryMessageDto {
    private String id;
    private String userId;
    private String userMessage;
    private String aiResponse;
    private String timestamp;
}