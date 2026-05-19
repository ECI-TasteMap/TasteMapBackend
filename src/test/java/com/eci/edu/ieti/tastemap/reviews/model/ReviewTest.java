package com.eci.edu.ieti.tastemap.reviews.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReviewTest {

    @Test
    void testReview() {
        Review review = new Review();
        review.setId("1");
        review.setUserId("user1");
        review.setRestaurantId("restaurant1");
        review.setLocationId("location1");
        review.setComment("Great!");
        review.setStars(5);

        assertEquals("1", review.getId());
        assertEquals("user1", review.getUserId());
        assertEquals("restaurant1", review.getRestaurantId());
        assertEquals("location1", review.getLocationId());
        assertEquals("Great!", review.getComment());
        assertEquals(5, review.getStars());
    }
}
