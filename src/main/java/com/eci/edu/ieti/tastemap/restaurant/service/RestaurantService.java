package com.eci.edu.ieti.tastemap.restaurant.service;

import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantRequestDto;
import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantOpenStatusResponseDto;
import com.eci.edu.ieti.tastemap.restaurant.model.Location;
import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for restaurant-related operations.
 */
public interface RestaurantService {
    Restaurant create(RestaurantRequestDto restaurantRequestDto);
    Restaurant create(RestaurantRequestDto restaurantRequestDto,
                      MultipartFile logoFile,
                      MultipartFile menuFile);
    Optional<Restaurant> findById(String id);
    List<Restaurant> all();
    String getOpenStatus(Location location);
    default String getOpenStatus(List<Location> locations) {
        if (locations == null || locations.isEmpty()) {
            return "CERRADO";
        }

        return locations.stream().anyMatch(location -> "ABIERTO".equals(getOpenStatus(location))) ? "ABIERTO" : "CERRADO";
    }
    RestaurantOpenStatusResponseDto getOpenStatusByRestaurantId(String id);
    void deleteById(String id);
    Restaurant update(String id,
                      RestaurantRequestDto restaurantRequestDto,
                      MultipartFile logoFile,
                      MultipartFile menuFile);
}

