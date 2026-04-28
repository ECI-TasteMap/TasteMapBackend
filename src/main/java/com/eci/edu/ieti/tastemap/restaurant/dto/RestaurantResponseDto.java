package com.eci.edu.ieti.tastemap.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for restaurant data in responses.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantResponseDto {
    private String id;
    private String ownerId;
    private String name;
    private String description;
    private String logo;
    private String menu;
    private String theme;
    private String idComment;
    private String hour;
}

