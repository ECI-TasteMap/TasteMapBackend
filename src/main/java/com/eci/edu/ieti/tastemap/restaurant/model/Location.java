package com.eci.edu.ieti.tastemap.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents the location of a restaurant.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "locations")
public class Location {

    @Id
    private String id;
    private String restaurantId;
    private String geolocation;
}

