package com.eci.edu.ieti.tastemap.restaurant.service;

import com.eci.edu.ieti.tastemap.restaurant.dto.LocationOpenStatusResponseDto;
import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantRequestDto;
import com.eci.edu.ieti.tastemap.restaurant.dto.RestaurantOpenStatusResponseDto;
import com.eci.edu.ieti.tastemap.restaurant.exception.RestaurantNotFoundException;
import com.eci.edu.ieti.tastemap.restaurant.mapper.RestaurantMapper;
import com.eci.edu.ieti.tastemap.restaurant.model.Location;
import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;
import com.eci.edu.ieti.tastemap.restaurant.model.Schedule;
import com.eci.edu.ieti.tastemap.restaurant.repository.RestaurantRepository;
import com.eci.edu.ieti.tastemap.restaurant.service.AzureStorageService;
import com.eci.edu.ieti.tastemap.restaurant.service.serviceImpl.RestaurantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @Mock
    private AzureStorageService azureStorageService;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private Restaurant restaurant;
    private RestaurantRequestDto restaurantRequestDto;

    @BeforeEach
    void setUp() {
        restaurant = buildRestaurant();
        restaurantRequestDto = buildRequestDto();
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
        void testCreateMultipart() {
        org.springframework.mock.web.MockMultipartFile logoFile = new org.springframework.mock.web.MockMultipartFile(
            "logo",
            "logo.png",
            "image/png",
            "logo-content".getBytes()
        );
        org.springframework.mock.web.MockMultipartFile menuFile = new org.springframework.mock.web.MockMultipartFile(
            "menu",
            "menu.pdf",
            "application/pdf",
            "menu-content".getBytes()
        );

        when(azureStorageService.uploadImage(logoFile)).thenReturn("https://blob/logo.png");
        when(azureStorageService.uploadMenu(menuFile)).thenReturn("https://blob/menu.pdf");
        when(restaurantMapper.toRestaurant(any(RestaurantRequestDto.class))).thenReturn(restaurant);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        Restaurant createdRestaurant = restaurantService.create(restaurantRequestDto, logoFile, menuFile);

        assertEquals(restaurant, createdRestaurant);
        verify(azureStorageService, times(1)).uploadImage(logoFile);
        verify(azureStorageService, times(1)).uploadMenu(menuFile);
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
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
    void testGetOpenStatusByRestaurantId() {
        when(restaurantRepository.findById("1")).thenReturn(Optional.of(restaurant));

        RestaurantOpenStatusResponseDto response = restaurantService.getOpenStatusByRestaurantId("1");

        assertEquals("1", response.getRestaurantId());
        assertEquals(1, response.getLocationStatuses().size());
        assertEquals("ABIERTO", response.getLocationStatuses().get(0).getOpenStatus());
        verify(restaurantRepository, times(1)).findById("1");
    }

    @Test
    void testGetOpenStatusByRestaurantIdNotFound() {
        when(restaurantRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> restaurantService.getOpenStatusByRestaurantId("1"));
        verify(restaurantRepository, times(1)).findById("1");
    }

    @Test
    void testDeleteById() {
        when(restaurantRepository.findById("1")).thenReturn(Optional.of(restaurant));
        doNothing().when(restaurantRepository).deleteById("1");

        restaurantService.deleteById("1");

        verify(restaurantRepository, times(1)).deleteById("1");
        verify(azureStorageService, times(1)).deleteFileByUrl("logo.png");
        verify(azureStorageService, times(1)).deleteFileByUrl("menu.pdf");
    }

    @Test
    void testDeleteByIdNotFound() {
        when(restaurantRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> restaurantService.deleteById("1"));
        verify(restaurantRepository, never()).deleteById("1");
    }

    @Test
    void testUpdate() {
        when(restaurantRepository.findById("1")).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        Restaurant updatedRestaurant = restaurantService.update("1", restaurantRequestDto, null, null);

        assertEquals(restaurant, updatedRestaurant);
        verify(restaurantRepository, times(1)).save(restaurant);
        verify(azureStorageService, never()).deleteFileByUrl(anyString());
    }

    @Test
    void testUpdateRemovesFilesWhenUrlRemoved() {
        org.springframework.mock.web.MockMultipartFile logoFile = new org.springframework.mock.web.MockMultipartFile(
            "logo",
            "new-logo.png",
            "image/png",
            "new-logo-content".getBytes()
        );
        org.springframework.mock.web.MockMultipartFile menuFile = new org.springframework.mock.web.MockMultipartFile(
            "menu",
            "new-menu.pdf",
            "application/pdf",
            "new-menu-content".getBytes()
        );

        when(restaurantRepository.findById("1")).thenReturn(Optional.of(restaurant));
        when(azureStorageService.uploadImage(logoFile)).thenReturn("https://blob/new-logo.png");
        when(azureStorageService.uploadMenu(menuFile)).thenReturn("https://blob/new-menu.pdf");
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        RestaurantRequestDto updatedRequest = new RestaurantRequestDto();
        updatedRequest.setName("Updated Name");

        restaurantService.update("1", updatedRequest, logoFile, menuFile);

        verify(azureStorageService, times(1)).deleteFileByUrl("logo.png");
        verify(azureStorageService, times(1)).deleteFileByUrl("menu.pdf");
    }

    @Test
    void testUpdateNotFound() {
        when(restaurantRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> restaurantService.update("1", restaurantRequestDto, null, null));
        verify(restaurantRepository, never()).save(any(Restaurant.class));
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
        value.setLocations(List.of(buildOpenLocation()));
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
        value.setLocations(List.of(buildOpenLocation()));
        value.setTags(Set.of("Italian", "Family"));
        value.setPriceMin(10);
        value.setPriceMax(30);
        return value;
    }

    private Location buildOpenLocation() {
        DayOfWeek today = LocalDate.now(ZoneId.of("America/Bogota")).getDayOfWeek();
        Schedule schedule = new Schedule(Set.of(today), "00:00", "23:59", false);
        return new Location("loc-1", "North", "3001234567", 4.5, List.of(schedule));
    }
}

