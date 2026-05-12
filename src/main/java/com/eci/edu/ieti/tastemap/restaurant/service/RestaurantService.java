package com.eci.edu.ieti.tastemap.restaurant.service;

import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantRequestDto;
import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantOpenStatusResponseDto;
import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for restaurant-related operations.
 */
public interface RestaurantService {
    Restaurant create(RestaurantRequestDto restaurantRequestDto);
    Restaurant create(String ownerId,
                      String name,
                      String phone,
                      String description,
                      String theme,
                      List<String> locations,
                      List<String> tags,
                      Integer priceMin,
                      Integer priceMax,
                      String hour,
                      MultipartFile logoFile,
                      MultipartFile menuFile);
    Optional<Restaurant> findById(String id);
    List<Restaurant> all();
    String getOpenStatus(String hourRange);
    RestaurantOpenStatusResponseDto getOpenStatusByRestaurantId(String id);
    void deleteById(String id);
    Restaurant update(String id,
                      String ownerId,
                      String name,
                      String phone,
                      String description,
                      String theme,
                      List<String> locations,
                      List<String> tags,
                      Integer priceMin,
                      Integer priceMax,
                      String hour,
                      MultipartFile logoFile,
                      MultipartFile menuFile);
}

