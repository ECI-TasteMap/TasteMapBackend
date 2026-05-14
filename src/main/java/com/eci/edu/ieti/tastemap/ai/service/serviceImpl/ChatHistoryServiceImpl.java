package com.eci.edu.ieti.tastemap.ai.service.serviceImpl;

import com.eci.edu.ieti.tastemap.ai.dto.ChatHistoryDeleteResponseDto;
import com.eci.edu.ieti.tastemap.ai.dto.ChatHistoryHistoryResponseDto;
import com.eci.edu.ieti.tastemap.ai.dto.ChatHistoryMessageDto;
import com.eci.edu.ieti.tastemap.ai.model.ChatHistory;
import com.eci.edu.ieti.tastemap.ai.service.ChatHistoryService;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatHistoryServiceImpl implements ChatHistoryService {

    private final MongoTemplate mongoTemplate;

        public ChatHistoryServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ChatHistoryHistoryResponseDto getHistory(String userId, int limit, long offset) {
        int effectiveLimit = Math.max(1, limit);
        long effectiveOffset = Math.max(0L, offset);

        Query query = new Query(Criteria.where("hist_userId").is(userId))
                .with(Sort.by(Sort.Direction.DESC, "hist_timestamp"))
                .skip(effectiveOffset)
                .limit(effectiveLimit);

        List<ChatHistoryMessageDto> messages = mongoTemplate.find(query, ChatHistory.class).stream()
                .map(chatHistory -> ChatHistoryMessageDto.builder()
                        .id(chatHistory.getId())
                        .userId(chatHistory.getUserId())
                        .userMessage(chatHistory.getUserMessage())
                        .aiResponse(chatHistory.getAiResponse())
                        .timestamp(chatHistory.getTimestamp())
                        .build())
                .toList();

        long total = mongoTemplate.count(new Query(Criteria.where("hist_userId").is(userId)), ChatHistory.class);
        boolean hasMore = effectiveOffset + messages.size() < total;

        return ChatHistoryHistoryResponseDto.builder()
                .success(true)
                .data(ChatHistoryHistoryResponseDto.ChatHistoryDataDto.builder()
                        .messages(messages)
                        .total(total)
                        .hasMore(hasMore)
                        .build())
                .build();
    }

    @Override
    public ChatHistoryDeleteResponseDto deleteHistory(String userId) {
        Query query = new Query(Criteria.where("hist_userId").is(userId));
        long deletedCount = mongoTemplate.remove(query, ChatHistory.class).getDeletedCount();

        return ChatHistoryDeleteResponseDto.builder()
                .success(true)
                .message("Historial limpiado")
                .deletedCount(deletedCount)
                .build();
    }
}