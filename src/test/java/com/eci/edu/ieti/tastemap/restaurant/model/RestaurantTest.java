package com.eci.edu.ieti.tastemap.restaurant.model;

import org.junit.jupiter.api.Test;

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
        restaurant.setTheme("Theme");
        restaurant.setLocations(Set.of("North", "Downtown"));
        restaurant.setTags(Set.of("Italian", "Family"));
        restaurant.setPriceMin(10);
        restaurant.setPriceMax(30);
        restaurant.setHour("9-5");

        assertEquals("1", restaurant.getId());
        assertEquals("owner1", restaurant.getOwnerId());
        assertEquals("Test Restaurant", restaurant.getName());
        assertEquals("Description", restaurant.getDescription());
        assertEquals("logo.png", restaurant.getLogo());
        assertEquals("menu.pdf", restaurant.getMenu());
        assertEquals("Theme", restaurant.getTheme());
        assertEquals(Set.of("North", "Downtown"), restaurant.getLocations());
        assertEquals(Set.of("Italian", "Family"), restaurant.getTags());
        assertEquals(10, restaurant.getPriceMin());
        assertEquals(30, restaurant.getPriceMax());
        assertEquals("9-5", restaurant.getHour());
        restaurant.setPhone("3001234567");
        assertEquals("3001234567", restaurant.getPhone());
    }
}

