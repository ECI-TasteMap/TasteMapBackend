package com.eci.edu.ieti.tastemap.user.service;

import com.eci.edu.ieti.tastemap.user.dto.ChatRequestDto;
import com.eci.edu.ieti.tastemap.user.dto.ChatResponseDto;

public interface ChatService {
    ChatResponseDto sendMessage(String userId, ChatRequestDto request);
}