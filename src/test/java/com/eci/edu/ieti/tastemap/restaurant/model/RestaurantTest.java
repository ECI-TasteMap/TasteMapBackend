package com.eci.edu.ieti.tastemap.restaurant.model;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestaurantTest {

    @Test
    void testRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId("1");
        restaurant.setOwnerId("owner1");
        restaurant.setName("Test Restaurant");
        restaurant.setDescription("Description");
        restaurant.setLogo("logo.png");
        restaurant.setMenu("menu.pdf");
        restaurant.setTheme(List.of("Theme"));
        restaurant.setLocations(List.of(buildLocation()));
        restaurant.setTags(Set.of("Italian", "Family"));
        restaurant.setPriceMin(10);
        restaurant.setPriceMax(30);

        assertEquals("1", restaurant.getId());
        assertEquals("owner1", restaurant.getOwnerId());
        assertEquals("Test Restaurant", restaurant.getName());
        assertEquals("Description", restaurant.getDescription());
        assertEquals("logo.png", restaurant.getLogo());
        assertEquals("menu.pdf", restaurant.getMenu());
        assertEquals(List.of("Theme"), restaurant.getTheme());
        assertEquals(List.of(buildLocation()), restaurant.getLocations());
        assertEquals(Set.of("Italian", "Family"), restaurant.getTags());
        assertEquals(10, restaurant.getPriceMin());
        assertEquals(30, restaurant.getPriceMax());
    }

    private Location buildLocation() {
        DayOfWeek today = LocalDate.now(ZoneId.of("America/Bogota")).getDayOfWeek();
        Schedule schedule = new Schedule(Set.of(today), "00:00", "23:59", false);
        return new Location("loc-1", "North", "3001234567", 4.5, List.of(schedule));
    }
}

