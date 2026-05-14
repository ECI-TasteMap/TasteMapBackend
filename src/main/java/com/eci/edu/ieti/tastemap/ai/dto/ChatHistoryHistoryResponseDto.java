package com.eci.edu.ieti.tastemap.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistoryHistoryResponseDto {
    private boolean success;
    private ChatHistoryDataDto data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatHistoryDataDto {
        private List<ChatHistoryMessageDto> messages;
        private long total;
        private boolean hasMore;
    }
}