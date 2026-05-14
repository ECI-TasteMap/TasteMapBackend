package com.eci.edu.ieti.tastemap.reviews.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for average stars of a restaurant.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewAverageResponseDto {
    private String restaurantId;
    private double average;
}