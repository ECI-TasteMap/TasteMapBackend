package com.eci.edu.ieti.tastemap.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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
    private String phone;
    private String description;
    private String logo;
    private String menu;
    private String theme;
    private Set<String> locations;
    private Set<String> tags;
    private Integer priceMin;
    private Integer priceMax;
    private String hour;
    private String openStatus;
}

