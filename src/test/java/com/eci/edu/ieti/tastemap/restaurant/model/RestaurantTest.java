package com.eci.edu.ieti.tastemap.restaurant.model;

import org.junit.jupiter.api.Test;

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
        restaurant.setIdComment("comment1");
        restaurant.setHour("9-5");

        assertEquals("1", restaurant.getId());
        assertEquals("owner1", restaurant.getOwnerId());
        assertEquals("Test Restaurant", restaurant.getName());
        assertEquals("Description", restaurant.getDescription());
        assertEquals("logo.png", restaurant.getLogo());
        assertEquals("menu.pdf", restaurant.getMenu());
        assertEquals("Theme", restaurant.getTheme());
        assertEquals("comment1", restaurant.getIdComment());
        assertEquals("9-5", restaurant.getHour());
    }
}

