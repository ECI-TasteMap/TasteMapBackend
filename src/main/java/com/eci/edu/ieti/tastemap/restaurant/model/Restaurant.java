package com.eci.edu.ieti.tastemap.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.eci.edu.ieti.tastemap.restaurant.model.Location;
import java.util.List;
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
    private List<String> theme;
    private List<Location> locations;
    private Set<String> tags;
    private Integer priceMin;
    private Integer priceMax;
}

