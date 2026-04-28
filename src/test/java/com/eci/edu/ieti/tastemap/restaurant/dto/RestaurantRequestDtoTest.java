package com.eci.edu.ieti.tastemap.restaurant.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestaurantRequestDtoTest {

    @Test
    void testRestaurantRequestDto() {
        RestaurantRequestDto dto = new RestaurantRequestDto();
        dto.setOwnerId("owner1");
        dto.setName("Test Restaurant");
        dto.setDescription("Description");
        dto.setLogo("logo.png");
        dto.setMenu("menu.pdf");
        dto.setTheme("Theme");
        dto.setIdComment("comment1");
        dto.setHour("9-5");

        assertEquals("owner1", dto.getOwnerId());
        assertEquals("Test Restaurant", dto.getName());
        assertEquals("Description", dto.getDescription());
        assertEquals("logo.png", dto.getLogo());
        assertEquals("menu.pdf", dto.getMenu());
        assertEquals("Theme", dto.getTheme());
        assertEquals("comment1", dto.getIdComment());
        assertEquals("9-5", dto.getHour());
    }
}

