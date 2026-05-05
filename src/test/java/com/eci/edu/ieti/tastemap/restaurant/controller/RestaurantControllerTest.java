package com.eci.edu.ieti.tastemap.restaurant.controller;

import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantRequestDto;
import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantResponseDto;
import com.eci.edu.ieti.tastemap.restaurant.mapper.RestaurantMapper;
import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;
import com.eci.edu.ieti.tastemap.restaurant.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantController restaurantController;

    private Restaurant restaurant;
    private RestaurantRequestDto restaurantRequestDto;
    private RestaurantResponseDto restaurantResponseDto;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant("1", "owner1", "Test Restaurant", "Description", "logo.png", "menu.pdf", "Theme", Set.of("North", "Downtown"), Set.of("Italian", "Family"), 10, 30, "9-5");
        restaurantRequestDto = new RestaurantRequestDto("owner1", "Test Restaurant", "Description", "logo.png", "menu.pdf", "Theme", Set.of("North", "Downtown"), Set.of("Italian", "Family"), 10, 30, "9-5");
        restaurantResponseDto = new RestaurantResponseDto("1", "owner1", "Test Restaurant", "Description", "logo.png", "menu.pdf", "Theme", Set.of("North", "Downtown"), Set.of("Italian", "Family"), 10, 30, "9-5");
    }

    @Test
    void testCreate() {
        when(restaurantService.create(restaurantRequestDto)).thenReturn(restaurant);
        when(restaurantMapper.toRestaurantResponseDto(restaurant)).thenReturn(restaurantResponseDto);

        ResponseEntity<RestaurantResponseDto> response = restaurantController.create(restaurantRequestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(restaurantResponseDto, response.getBody());
        verify(restaurantService, times(1)).create(restaurantRequestDto);
    }

    @Test
    void testFindById() {
        when(restaurantService.findById("1")).thenReturn(Optional.of(restaurant));
        when(restaurantMapper.toRestaurantResponseDto(restaurant)).thenReturn(restaurantResponseDto);

        ResponseEntity<RestaurantResponseDto> response = restaurantController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(restaurantResponseDto, response.getBody());
        verify(restaurantService, times(1)).findById("1");
    }

    @Test
    void testAll() {
        when(restaurantService.all()).thenReturn(Collections.singletonList(restaurant));
        when(restaurantMapper.toRestaurantResponseDto(restaurant)).thenReturn(restaurantResponseDto);

        ResponseEntity<List<RestaurantResponseDto>> response = restaurantController.all();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(restaurantResponseDto, response.getBody().get(0));
        verify(restaurantService, times(1)).all();
    }

    @Test
    void testUpdate() {
        when(restaurantService.update("1", restaurantRequestDto)).thenReturn(restaurant);
        when(restaurantMapper.toRestaurantResponseDto(restaurant)).thenReturn(restaurantResponseDto);

        ResponseEntity<RestaurantResponseDto> response = restaurantController.update("1", restaurantRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(restaurantResponseDto, response.getBody());
        verify(restaurantService, times(1)).update("1", restaurantRequestDto);
    }

    @Test
    void testDeleteById() {
        doNothing().when(restaurantService).deleteById("1");

        ResponseEntity<Void> response = restaurantController.deleteById("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(restaurantService, times(1)).deleteById("1");
    }
}

