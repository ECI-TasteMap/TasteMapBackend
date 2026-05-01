package com.eci.edu.ieti.tastemap.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * Represents a restaurant in the TasteMap application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "restaurants")
public class Restaurant {

    @Id
    private String id;
    private String ownerId;
    private String name;
    private String description;
    private String logo;
    private String menu;
    private String theme;
    private Set<String> locations;
    private Set<String> tags;
    private Integer priceMin;
    private Integer priceMax;
    private String hour;
}

