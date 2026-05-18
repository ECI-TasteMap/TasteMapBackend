package com.eci.edu.ieti.tastemap.restaurant.dto;

import com.eci.edu.ieti.tastemap.restaurant.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
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
    private String description;
    private String logo;
    private String menu;
    private List<String> theme;
    private List<Location> locations;
    private Set<String> tags;
    private Integer priceMin;
    private Integer priceMax;
    private String openStatus;
}

