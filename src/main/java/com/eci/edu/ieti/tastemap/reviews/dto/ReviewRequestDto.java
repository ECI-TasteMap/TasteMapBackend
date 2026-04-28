package com.eci.edu.ieti.tastemap.reviews.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for review creation and update requests.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDto {
    private String userId;
    private String restaurantId;
    private String comment;
    private int stars;
}

