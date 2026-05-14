package com.eci.edu.ieti.tastemap.restaurant.mapper;

import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantRequestDto;
import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantResponseDto;
import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for converting between Restaurant, RestaurantRequestDto, and RestaurantResponseDto.
 */
@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    @Mapping(target = "id", ignore = true)
    Restaurant toRestaurant(RestaurantRequestDto restaurantRequestDto);

    @Mapping(target = "openStatus", ignore = true)
    RestaurantResponseDto toRestaurantResponseDto(Restaurant restaurant);
}

