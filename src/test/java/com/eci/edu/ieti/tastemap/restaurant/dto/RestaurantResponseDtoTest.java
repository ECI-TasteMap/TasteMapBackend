package com.eci.edu.ieti.tastemap.restaurant.dto;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestaurantResponseDtoTest {

    @Test
    void testRestaurantResponseDto() {
        RestaurantResponseDto dto = new RestaurantResponseDto();
        dto.setId("1");
        dto.setOwnerId("owner1");
        dto.setName("Test Restaurant");
        dto.setDescription("Description");
        dto.setLogo("logo.png");
        dto.setMenu("menu.pdf");
        dto.setTheme("Theme");
        dto.setLocations(Set.of("North", "Downtown"));
        dto.setTags(Set.of("Italian", "Family"));
        dto.setPriceMin(10);
        dto.setPriceMax(30);
        dto.setHour("9-5");

        assertEquals("1", dto.getId());
        assertEquals("owner1", dto.getOwnerId());
        assertEquals("Test Restaurant", dto.getName());
        assertEquals("Description", dto.getDescription());
        assertEquals("logo.png", dto.getLogo());
        assertEquals("menu.pdf", dto.getMenu());
        assertEquals("Theme", dto.getTheme());
        assertEquals(Set.of("North", "Downtown"), dto.getLocations());
        assertEquals(Set.of("Italian", "Family"), dto.getTags());
        assertEquals(10, dto.getPriceMin());
        assertEquals(30, dto.getPriceMax());
        assertEquals("9-5", dto.getHour());
    }
}

