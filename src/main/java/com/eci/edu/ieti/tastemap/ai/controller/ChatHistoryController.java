package com.eci.edu.ieti.tastemap.ai.controller;

import com.eci.edu.ieti.tastemap.ai.dto.ChatHistoryDeleteResponseDto;
import com.eci.edu.ieti.tastemap.ai.dto.ChatHistoryHistoryResponseDto;
import com.eci.edu.ieti.tastemap.ai.service.ChatHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/history")
@CrossOrigin(origins = "*")
public class ChatHistoryController {

    private final ChatHistoryService chatHistoryService;

    public ChatHistoryController(ChatHistoryService chatHistoryService) {
        this.chatHistoryService = chatHistoryService;
    }

    @GetMapping
    public ResponseEntity<ChatHistoryHistoryResponseDto> getHistory(
            @RequestParam String userId,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") long offset) {
        return ResponseEntity.ok(chatHistoryService.getHistory(userId, limit, offset));
    }

    @DeleteMapping
    public ResponseEntity<ChatHistoryDeleteResponseDto> deleteHistory(@RequestParam String userId) {
        return ResponseEntity.ok(chatHistoryService.deleteHistory(userId));
    }
}