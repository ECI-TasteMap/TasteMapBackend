package com.eci.edu.ieti.tastemap.restaurant.controller;

import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantResponseDto;
import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantOpenStatusResponseDto;
import com.eci.edu.ieti.tastemap.restaurant.exception.RestaurantNotFoundException;
import com.eci.edu.ieti.tastemap.restaurant.mapper.RestaurantMapper;
import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;
import com.eci.edu.ieti.tastemap.restaurant.service.RestaurantService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestaurantResponseDto> create(
            @RequestParam String ownerId,
            @RequestParam String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String theme,
            @RequestParam(required = false) List<String> locations,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) Integer priceMin,
            @RequestParam(required = false) Integer priceMax,
            @RequestParam(required = false) String hour,
            @RequestParam(value = "logo", required = false) org.springframework.web.multipart.MultipartFile logoFile,
            @RequestParam(value = "menu", required = false) org.springframework.web.multipart.MultipartFile menuFile) {

        Restaurant restaurant = restaurantService.create(
                ownerId,
                name,
                phone,
                description,
                theme,
                locations,
                tags,
                priceMin,
                priceMax,
                hour,
                logoFile,
                menuFile
        );
        RestaurantResponseDto restaurantResponseDto = enrichRestaurantResponseDto(restaurantMapper.toRestaurantResponseDto(restaurant));
        return ResponseEntity.created(URI.create("/api/v1/restaurants/" + restaurant.getId())).body(restaurantResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> findById(@PathVariable String id) {
        Restaurant restaurant = restaurantService.findById(id).orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + id + " not found"));
        RestaurantResponseDto restaurantResponseDto = enrichRestaurantResponseDto(restaurantMapper.toRestaurantResponseDto(restaurant));
        return ResponseEntity.ok(restaurantResponseDto);
    }

    @GetMapping("/{id}/open-status")
    public ResponseEntity<RestaurantOpenStatusResponseDto> openStatus(@PathVariable String id) {
        return ResponseEntity.ok(restaurantService.getOpenStatusByRestaurantId(id));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDto>> all() {
        List<Restaurant> restaurants = restaurantService.all();
        List<RestaurantResponseDto> restaurantResponseDtos = restaurants.stream()
                .map(restaurantMapper::toRestaurantResponseDto)
                .map(this::enrichRestaurantResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(restaurantResponseDtos);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestaurantResponseDto> updateMultipart(
            @PathVariable String id,
            @RequestParam(required = false) String ownerId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String theme,
            @RequestParam(required = false) List<String> locations,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) Integer priceMin,
            @RequestParam(required = false) Integer priceMax,
            @RequestParam(required = false) String hour,
            @RequestParam(value = "logo", required = false) MultipartFile logoFile,
            @RequestParam(value = "menu", required = false) MultipartFile menuFile) {
        Restaurant restaurant = restaurantService.update(
                id,
                ownerId,
                name,
                phone,
                description,
                theme,
                locations,
                tags,
                priceMin,
                priceMax,
                hour,
                logoFile,
                menuFile
        );
        RestaurantResponseDto restaurantResponseDto = enrichRestaurantResponseDto(restaurantMapper.toRestaurantResponseDto(restaurant));
        return ResponseEntity.ok(restaurantResponseDto);
    }

    private RestaurantResponseDto enrichRestaurantResponseDto(RestaurantResponseDto dto) {
        dto.setOpenStatus(restaurantService.getOpenStatus(dto.getHour()));
        return dto;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        restaurantService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

