package com.eci.edu.ieti.tastemap.reviews.service;

import com.eci.edu.ieti.tastemap.reviews.dto.ReviewRequestDto;
import com.eci.edu.ieti.tastemap.reviews.model.Review;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for review-related operations.
 */
public interface ReviewService {
    Review create(ReviewRequestDto reviewRequestDto);
    Optional<Review> findById(String id);
    List<Review> all();
    List<Review> findByRestaurantId(String restaurantId);
    List<Review> findByUserId(String userId);
    double averageByRestaurantId(String restaurantId);
    void deleteById(String id);
    Review update(String id, ReviewRequestDto reviewRequestDto);
}

