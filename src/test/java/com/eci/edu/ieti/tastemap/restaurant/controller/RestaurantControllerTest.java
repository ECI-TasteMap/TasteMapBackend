package com.eci.edu.ieti.tastemap.restaurant.controller;

import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantResponseDto;
import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantOpenStatusResponseDto;
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
    private RestaurantResponseDto restaurantResponseDto;
    private RestaurantOpenStatusResponseDto restaurantOpenStatusResponseDto;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant("1", "owner1", "Test Restaurant", "3001234567", "Description", "logo.png", "menu.pdf", "Theme", Set.of("North", "Downtown"), Set.of("Italian", "Family"), 10, 30, "9-5", null);
        restaurantResponseDto = new RestaurantResponseDto("1", "owner1", "Test Restaurant", "3001234567", "Description", "logo.png", "menu.pdf", "Theme", Set.of("North", "Downtown"), Set.of("Italian", "Family"), 10, 30, "9-5", null, null);
        restaurantOpenStatusResponseDto = new RestaurantOpenStatusResponseDto("1", "ABIERTO");
    }

    @Test
    void testUploadLogo() {
        // upload endpoint was removed and logic merged into create; nothing to test here
    }

    @Test
    void testCreate() {
        when(restaurantService.create(
            anyString(),
            anyString(),
            any(),
            any(),
            any(),
            anyList(),
            anyList(),
            any(),
            any(),
            any(),
            isNull(),
            isNull()
        )).thenReturn(restaurant);
        when(restaurantMapper.toRestaurantResponseDto(restaurant)).thenReturn(restaurantResponseDto);

        ResponseEntity<RestaurantResponseDto> response = restaurantController.create(
            "owner1",
            "Test Restaurant",
            "3001234567",
            "Description",
            "Theme",
            List.of("North", "Downtown"),
            List.of("Italian", "Family"),
            10,
            30,
            "9-5",
            null,
            null
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(restaurantResponseDto, response.getBody());
        verify(restaurantService, times(1)).create(
            eq("owner1"),
            eq("Test Restaurant"),
            eq("3001234567"),
            eq("Description"),
            eq("Theme"),
            eq(List.of("North", "Downtown")),
            eq(List.of("Italian", "Family")),
            eq(10),
            eq(30),
            eq("9-5"),
            isNull(),
            isNull()
        );
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
    void testOpenStatus() {
        when(restaurantService.getOpenStatusByRestaurantId("1")).thenReturn(restaurantOpenStatusResponseDto);

        ResponseEntity<RestaurantOpenStatusResponseDto> response = restaurantController.openStatus("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(restaurantOpenStatusResponseDto, response.getBody());
        verify(restaurantService, times(1)).getOpenStatusByRestaurantId("1");
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
        void testUpdateMultipart() {
        when(restaurantService.update(
            eq("1"),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            isNull(),
            isNull()
        )).thenReturn(restaurant);
        when(restaurantMapper.toRestaurantResponseDto(restaurant)).thenReturn(restaurantResponseDto);

        ResponseEntity<RestaurantResponseDto> response = restaurantController.updateMultipart(
            "1",
            null,
            "Updated Name",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(restaurantResponseDto, response.getBody());
        verify(restaurantService, times(1)).update(
            eq("1"),
            isNull(),
            eq("Updated Name"),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull()
        );
        }

    @Test
    void testDeleteById() {
        doNothing().when(restaurantService).deleteById("1");

        ResponseEntity<Void> response = restaurantController.deleteById("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(restaurantService, times(1)).deleteById("1");
    }
}

