package com.eci.edu.ieti.tastemap.ai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_history")
public class ChatHistory {

    @Id
    private String id;

    @Field("hist_userId")
    private String userId;

    @Field("hist_userMessage")
    private String userMessage;

    @Field("hist_aiResponse")
    private String aiResponse;

    @Field("hist_timestamp")
    private String timestamp;
}