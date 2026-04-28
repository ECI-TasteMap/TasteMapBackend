package com.eci.edu.ieti.tastemap.restaurant.controller;

import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantRequestDto;
import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantResponseDto;
import com.eci.edu.ieti.tastemap.restaurant.exception.RestaurantNotFoundException;
import com.eci.edu.ieti.tastemap.restaurant.mapper.RestaurantMapper;
import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;
import com.eci.edu.ieti.tastemap.restaurant.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing restaurants.
 */
@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    public RestaurantController(RestaurantService restaurantService, RestaurantMapper restaurantMapper) {
        this.restaurantService = restaurantService;
        this.restaurantMapper = restaurantMapper;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponseDto> create(@RequestBody RestaurantRequestDto restaurantRequestDto) {
        Restaurant restaurant = restaurantService.create(restaurantRequestDto);
        RestaurantResponseDto restaurantResponseDto = restaurantMapper.toRestaurantResponseDto(restaurant);
        return ResponseEntity.created(URI.create("/api/v1/restaurants/" + restaurant.getId())).body(restaurantResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> findById(@PathVariable String id) {
        Restaurant restaurant = restaurantService.findById(id).orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + id + " not found"));
        RestaurantResponseDto restaurantResponseDto = restaurantMapper.toRestaurantResponseDto(restaurant);
        return ResponseEntity.ok(restaurantResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDto>> all() {
        List<Restaurant> restaurants = restaurantService.all();
        List<RestaurantResponseDto> restaurantResponseDtos = restaurants.stream()
                .map(restaurantMapper::toRestaurantResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(restaurantResponseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> update(@PathVariable String id, @RequestBody RestaurantRequestDto restaurantRequestDto) {
        Restaurant restaurant = restaurantService.update(id, restaurantRequestDto);
        RestaurantResponseDto restaurantResponseDto = restaurantMapper.toRestaurantResponseDto(restaurant);
        return ResponseEntity.ok(restaurantResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        restaurantService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

