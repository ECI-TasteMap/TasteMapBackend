package com.eci.edu.ieti.tastemap.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for restaurant open status lookups.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOpenStatusResponseDto {
    private String restaurantId;
    private List<LocationOpenStatusResponseDto> locationStatuses;
}