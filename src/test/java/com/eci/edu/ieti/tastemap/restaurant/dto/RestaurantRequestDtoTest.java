package com.eci.edu.ieti.tastemap.restaurant.dto;

import com.eci.edu.ieti.tastemap.restaurant.model.Location;
import com.eci.edu.ieti.tastemap.restaurant.model.Schedule;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

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
        dto.setTheme(List.of("Theme"));
        dto.setLocations(List.of(buildLocation()));
        dto.setTags(Set.of("Italian", "Family"));
        dto.setPriceMin(10);
        dto.setPriceMax(30);

        assertEquals("owner1", dto.getOwnerId());
        assertEquals("Test Restaurant", dto.getName());
        assertEquals("Description", dto.getDescription());
        assertEquals("logo.png", dto.getLogo());
        assertEquals("menu.pdf", dto.getMenu());
        assertEquals(List.of("Theme"), dto.getTheme());
        assertEquals(List.of(buildLocation()), dto.getLocations());
        assertEquals(Set.of("Italian", "Family"), dto.getTags());
        assertEquals(10, dto.getPriceMin());
        assertEquals(30, dto.getPriceMax());
    }

    private Location buildLocation() {
        DayOfWeek today = LocalDate.now(ZoneId.of("America/Bogota")).getDayOfWeek();
        Schedule schedule = new Schedule(Set.of(today), "00:00", "23:59", false);
        return new Location("loc-1", "North", "3001234567", 4.5, List.of(schedule));
    }
}

