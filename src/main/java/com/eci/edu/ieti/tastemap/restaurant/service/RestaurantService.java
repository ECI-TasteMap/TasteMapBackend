package com.eci.edu.ieti.tastemap.restaurant.service;

import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantRequestDto;
import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for restaurant-related operations.
 */
public interface RestaurantService {
    Restaurant create(RestaurantRequestDto restaurantRequestDto);
    Optional<Restaurant> findById(String id);
    List<Restaurant> all();
    void deleteById(String id);
    Restaurant update(String id, RestaurantRequestDto restaurantRequestDto);
}

