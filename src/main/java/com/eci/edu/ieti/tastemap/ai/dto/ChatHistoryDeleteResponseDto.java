package com.eci.edu.ieti.tastemap.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistoryDeleteResponseDto {
    private boolean success;
    private String message;
    private long deletedCount;
}