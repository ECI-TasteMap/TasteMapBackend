package com.eci.edu.ieti.tastemap.ai.service;

import com.eci.edu.ieti.tastemap.ai.dto.ChatHistoryDeleteResponseDto;
import com.eci.edu.ieti.tastemap.ai.dto.ChatHistoryHistoryResponseDto;

public interface ChatHistoryService {
    ChatHistoryHistoryResponseDto getHistory(String userId, int limit, long offset);

    ChatHistoryDeleteResponseDto deleteHistory(String userId);
}