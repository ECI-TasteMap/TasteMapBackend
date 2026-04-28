package com.eci.edu.ieti.tastemap.restaurant.service;

import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantRequestDto;
import com.eci.edu.ieti.tastemap.restaurant.exception.RestaurantNotFoundException;
import com.eci.edu.ieti.tastemap.restaurant.mapper.RestaurantMapper;
import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;
import com.eci.edu.ieti.tastemap.restaurant.repository.RestaurantRepository;
import com.eci.edu.ieti.tastemap.restaurant.service.serviceImpl.RestaurantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private Restaurant restaurant;
    private RestaurantRequestDto restaurantRequestDto;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant("1", "owner1", "Test Restaurant", "Description", "logo.png", "menu.pdf", "Theme", "comment1", "9-5");
        restaurantRequestDto = new RestaurantRequestDto("owner1", "Test Restaurant", "Description", "logo.png", "menu.pdf", "Theme", "comment1", "9-5");
    }

    @Test
    void testCreate() {
        when(restaurantMapper.toRestaurant(restaurantRequestDto)).thenReturn(restaurant);
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        Restaurant createdRestaurant = restaurantService.create(restaurantRequestDto);

        assertEquals(restaurant, createdRestaurant);
        verify(restaurantRepository, times(1)).save(restaurant);
    }

    @Test
    void testFindById() {
        when(restaurantRepository.findById("1")).thenReturn(Optional.of(restaurant));

        Optional<Restaurant> foundRestaurant = restaurantService.findById("1");

        assertTrue(foundRestaurant.isPresent());
        assertEquals(restaurant, foundRestaurant.get());
        verify(restaurantRepository, times(1)).findById("1");
    }

    @Test
    void testAll() {
        when(restaurantRepository.findAll()).thenReturn(Collections.singletonList(restaurant));

        List<Restaurant> restaurants = restaurantService.all();

        assertEquals(1, restaurants.size());
        assertEquals(restaurant, restaurants.get(0));
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    void testDeleteById() {
        when(restaurantRepository.existsById("1")).thenReturn(true);
        doNothing().when(restaurantRepository).deleteById("1");

        restaurantService.deleteById("1");

        verify(restaurantRepository, times(1)).deleteById("1");
    }

    @Test
    void testDeleteByIdNotFound() {
        when(restaurantRepository.existsById("1")).thenReturn(false);

        assertThrows(RestaurantNotFoundException.class, () -> restaurantService.deleteById("1"));
        verify(restaurantRepository, never()).deleteById("1");
    }

    @Test
    void testUpdate() {
        when(restaurantRepository.findById("1")).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        Restaurant updatedRestaurant = restaurantService.update("1", restaurantRequestDto);

        assertEquals(restaurant, updatedRestaurant);
        verify(restaurantRepository, times(1)).save(restaurant);
    }

    @Test
    void testUpdateNotFound() {
        when(restaurantRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> restaurantService.update("1", restaurantRequestDto));
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }
}

