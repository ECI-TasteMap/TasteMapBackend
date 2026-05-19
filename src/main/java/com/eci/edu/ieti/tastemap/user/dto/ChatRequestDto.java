package com.eci.edu.ieti.tastemap.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDto {
    private String message;
    private List<Object> conversationHistory;
    private String token;

    public ChatRequestDto(String message, List<Object> conversationHistory) {
        this.message = message;
        this.conversationHistory = conversationHistory;
    }
}