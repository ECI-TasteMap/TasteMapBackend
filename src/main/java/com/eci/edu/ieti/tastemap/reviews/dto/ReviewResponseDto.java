package com.eci.edu.ieti.tastemap.reviews.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for review data in responses.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {
    private String id;
    private String userId;
    private String restaurantId;
    private String locationId;
    private String comment;
    private int stars;
}
