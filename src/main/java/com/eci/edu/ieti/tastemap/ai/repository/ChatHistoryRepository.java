package com.eci.edu.ieti.tastemap.ai.repository;

import com.eci.edu.ieti.tastemap.ai.model.ChatHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatHistoryRepository extends MongoRepository<ChatHistory, String> {
    long countByUserId(String userId);
}