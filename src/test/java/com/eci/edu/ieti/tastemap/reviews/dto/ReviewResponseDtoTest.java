package com.eci.edu.ieti.tastemap.reviews.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReviewResponseDtoTest {

    @Test
    void testReviewResponseDto() {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId("1");
        dto.setUserId("user1");
        dto.setRestaurantId("restaurant1");
        dto.setLocationId("location1");
        dto.setComment("Great!");
        dto.setStars(5);

        assertEquals("1", dto.getId());
        assertEquals("user1", dto.getUserId());
        assertEquals("restaurant1", dto.getRestaurantId());
        assertEquals("location1", dto.getLocationId());
        assertEquals("Great!", dto.getComment());
        assertEquals(5, dto.getStars());
    }
}
