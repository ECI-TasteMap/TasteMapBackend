package com.eci.edu.ieti.tastemap.restaurant.controller;

import com.eci.edu.ieti.tastemap.restaurant.dto.LocationOpenStatusResponseDto;
import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantRequestDto;
import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantOpenStatusResponseDto;
import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantResponseDto;
import com.eci.edu.ieti.tastemap.restaurant.mapper.RestaurantMapper;
import com.eci.edu.ieti.tastemap.restaurant.model.Location;
import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;
import com.eci.edu.ieti.tastemap.restaurant.model.Schedule;
import com.eci.edu.ieti.tastemap.restaurant.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
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
        restaurant = buildRestaurant();
        restaurantResponseDto = buildRestaurantResponseDto();
        restaurantOpenStatusResponseDto = new RestaurantOpenStatusResponseDto(
                "1",
                List.of(new LocationOpenStatusResponseDto(buildLocation(), "ABIERTO"))
        );
        lenient().when(restaurantService.getOpenStatus(anyList())).thenReturn("ABIERTO");
    }

    @Test
    void testUploadLogo() {
        // upload endpoint was removed and logic merged into create; nothing to test here
    }

    @Test
    void testCreate() {
        RestaurantRequestDto requestDto = buildRequestDto();
        when(restaurantService.create(any(RestaurantRequestDto.class), isNull(), isNull())).thenReturn(restaurant);
        when(restaurantMapper.toRestaurantResponseDto(restaurant)).thenReturn(restaurantResponseDto);

        ResponseEntity<RestaurantResponseDto> response = restaurantController.create(requestDto, null, null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(restaurantResponseDto, response.getBody());
        verify(restaurantService, times(1)).create(eq(requestDto), isNull(), isNull());
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
        RestaurantRequestDto requestDto = buildRequestDto();
        requestDto.setName("Updated Name");
        when(restaurantService.update(eq("1"), any(RestaurantRequestDto.class), isNull(), isNull())).thenReturn(restaurant);
        when(restaurantMapper.toRestaurantResponseDto(restaurant)).thenReturn(restaurantResponseDto);

        ResponseEntity<RestaurantResponseDto> response = restaurantController.updateMultipart("1", requestDto, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(restaurantResponseDto, response.getBody());
        verify(restaurantService, times(1)).update(eq("1"), eq(requestDto), isNull(), isNull());
    }

    @Test
    void testDeleteById() {
        doNothing().when(restaurantService).deleteById("1");

        ResponseEntity<Void> response = restaurantController.deleteById("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(restaurantService, times(1)).deleteById("1");
    }

    private Restaurant buildRestaurant() {
        Restaurant value = new Restaurant();
        value.setId("1");
        value.setOwnerId("owner1");
        value.setName("Test Restaurant");
        value.setDescription("Description");
        value.setLogo("logo.png");
        value.setMenu("menu.pdf");
        value.setTheme(List.of("Theme"));
        value.setLocations(List.of(buildLocation()));
        value.setTags(Set.of("Italian", "Family"));
        value.setPriceMin(10);
        value.setPriceMax(30);
        return value;
    }

    private RestaurantResponseDto buildRestaurantResponseDto() {
        RestaurantResponseDto value = new RestaurantResponseDto();
        value.setId("1");
        value.setOwnerId("owner1");
        value.setName("Test Restaurant");
        value.setDescription("Description");
        value.setLogo("logo.png");
        value.setMenu("menu.pdf");
        value.setTheme(List.of("Theme"));
        value.setLocations(List.of(buildLocation()));
        value.setTags(Set.of("Italian", "Family"));
        value.setPriceMin(10);
        value.setPriceMax(30);
        return value;
    }

    private RestaurantRequestDto buildRequestDto() {
        RestaurantRequestDto value = new RestaurantRequestDto();
        value.setOwnerId("owner1");
        value.setName("Test Restaurant");
        value.setDescription("Description");
        value.setLogo("logo.png");
        value.setMenu("menu.pdf");
        value.setTheme(List.of("Theme"));
        value.setLocations(List.of(buildLocation()));
        value.setTags(Set.of("Italian", "Family"));
        value.setPriceMin(10);
        value.setPriceMax(30);
        return value;
    }

    private Location buildLocation() {
        DayOfWeek today = LocalDate.now(ZoneId.of("America/Bogota")).getDayOfWeek();
        Schedule schedule = new Schedule(Set.of(today), "00:00", "23:59", false);
        return new Location("loc-1", "North", "3001234567", 4.5, List.of(schedule));
    }
}

