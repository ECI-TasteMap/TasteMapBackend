package com.eci.edu.ieti.tastemap.restaurant.controller;

import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantRequestDto;
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
@CrossOrigin(origins = "*")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    public RestaurantController(RestaurantService restaurantService, RestaurantMapper restaurantMapper) {
        this.restaurantService = restaurantService;
        this.restaurantMapper = restaurantMapper;
    }
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    /** Crea un nuevo restaurante (datos + opcional logo y menú). */
    public ResponseEntity<RestaurantResponseDto> create(
            @RequestPart("restaurant") RestaurantRequestDto restaurantRequestDto,
            @RequestPart(value = "logo", required = false) org.springframework.web.multipart.MultipartFile logoFile,
            @RequestPart(value = "menu", required = false) org.springframework.web.multipart.MultipartFile menuFile) {

        Restaurant restaurant = restaurantService.create(restaurantRequestDto, logoFile, menuFile);
        RestaurantResponseDto restaurantResponseDto = enrichRestaurantResponseDto(restaurantMapper.toRestaurantResponseDto(restaurant));
        return ResponseEntity.created(URI.create("/api/v1/restaurants/" + restaurant.getId())).body(restaurantResponseDto);
    }

    @GetMapping("/{id}")
    /** Devuelve un restaurante por su id. */
    public ResponseEntity<RestaurantResponseDto> findById(@PathVariable String id) {
        Restaurant restaurant = restaurantService.findById(id).orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + id + " not found"));
        RestaurantResponseDto restaurantResponseDto = enrichRestaurantResponseDto(restaurantMapper.toRestaurantResponseDto(restaurant));
        return ResponseEntity.ok(restaurantResponseDto);
    }

    @GetMapping("/{id}/open-status")
    /** Devuelve el estado de apertura actual del restaurante. */
    public ResponseEntity<RestaurantOpenStatusResponseDto> openStatus(@PathVariable String id) {
        return ResponseEntity.ok(restaurantService.getOpenStatusByRestaurantId(id));
    }

    @GetMapping
    /** Lista todos los restaurantes (incluye estado de apertura). */
    public ResponseEntity<List<RestaurantResponseDto>> all() {
        List<Restaurant> restaurants = restaurantService.all();
        List<RestaurantResponseDto> restaurantResponseDtos = restaurants.stream()
                .map(restaurantMapper::toRestaurantResponseDto)
                .map(this::enrichRestaurantResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(restaurantResponseDtos);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    /** Actualiza un restaurante (recibe datos y opcionalmente archivos multipart). */
    public ResponseEntity<RestaurantResponseDto> updateMultipart(
            @PathVariable String id,
            @RequestPart("restaurant") RestaurantRequestDto restaurantRequestDto,
            @RequestPart(value = "logo", required = false) MultipartFile logoFile,
            @RequestPart(value = "menu", required = false) MultipartFile menuFile) {
        Restaurant restaurant = restaurantService.update(id, restaurantRequestDto, logoFile, menuFile);
        RestaurantResponseDto restaurantResponseDto = enrichRestaurantResponseDto(restaurantMapper.toRestaurantResponseDto(restaurant));
        return ResponseEntity.ok(restaurantResponseDto);
    }

    private RestaurantResponseDto enrichRestaurantResponseDto(RestaurantResponseDto dto) {
        /** Añade información calculada de `openStatus` al DTO. */
        dto.setOpenStatus(restaurantService.getOpenStatus(dto.getLocations()));
        return dto;
    }

    @DeleteMapping("/{id}")
    /** Elimina un restaurante por su id. */
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        restaurantService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

