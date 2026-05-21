package com.eci.edu.ieti.tastemap.reviews.service.serviceImpl;

import com.eci.edu.ieti.tastemap.reviews.dto.ReviewRequestDto;
import com.eci.edu.ieti.tastemap.reviews.exception.ReviewNotFoundException;
import com.eci.edu.ieti.tastemap.reviews.mapper.ReviewMapper;
import com.eci.edu.ieti.tastemap.reviews.model.Review;
import com.eci.edu.ieti.tastemap.reviews.repository.ReviewRepository;
import com.eci.edu.ieti.tastemap.reviews.service.ReviewService;
import com.eci.edu.ieti.tastemap.restaurant.exception.RestaurantNotFoundException;
import com.eci.edu.ieti.tastemap.restaurant.model.Location;
import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;
import com.eci.edu.ieti.tastemap.restaurant.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the ReviewService interface.
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final RestaurantRepository restaurantRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewMapper reviewMapper, RestaurantRepository restaurantRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Review create(ReviewRequestDto reviewRequestDto) {
        Restaurant restaurant = restaurantRepository.findById(reviewRequestDto.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + reviewRequestDto.getRestaurantId() + " not found"));

        boolean locationExists = restaurant.getLocations().stream()
                .anyMatch(location -> location.getId().equals(reviewRequestDto.getLocationId()));

        if (!locationExists) {
            throw new RestaurantNotFoundException("Location with id " + reviewRequestDto.getLocationId() + " not found in restaurant " + reviewRequestDto.getRestaurantId());
        }

        Review review = reviewMapper.toReview(reviewRequestDto);
        Review savedReview = reviewRepository.save(review);
        refreshRestaurantAverage(savedReview.getRestaurantId());
        return savedReview;
    }

    @Override
    public Optional<Review> findById(String id) {
        return reviewRepository.findById(id);
    }

    @Override
    public List<Review> all() {
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> findByRestaurantId(String restaurantId) {
        return reviewRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public List<Review> findByUserId(String userId) {
        return reviewRepository.findByUserId(userId);
    }

    @Override
    public List<Review> findByLocationId(String locationId) {
        return reviewRepository.findByLocationId(locationId);
    }

    @Override
    public double averageByRestaurantId(String restaurantId) {
        List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);
        if (reviews.isEmpty()) {
            return 0.0;
        }

        double totalStars = reviews.stream()
                .mapToInt(Review::getStars)
                .sum();
        return totalStars / reviews.size();
    }

    @Override
    public void deleteById(String id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id " + id + " not found"));

        reviewRepository.deleteById(id);
        refreshRestaurantAverage(review.getRestaurantId());
    }

    @Override
    public Review update(String id, ReviewRequestDto reviewRequestDto) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ReviewNotFoundException("Review with id " + id + " not found"));
        String previousRestaurantId = review.getRestaurantId();
        review.setUserId(reviewRequestDto.getUserId());
        review.setRestaurantId(reviewRequestDto.getRestaurantId());
        review.setLocationId(reviewRequestDto.getLocationId());
        review.setComment(reviewRequestDto.getComment());
        review.setStars(reviewRequestDto.getStars());

        Review savedReview = reviewRepository.save(review);
        refreshRestaurantAverage(previousRestaurantId);
        if (!previousRestaurantId.equals(savedReview.getRestaurantId())) {
            refreshRestaurantAverage(savedReview.getRestaurantId());
        }
        return savedReview;
    }

    private void refreshRestaurantAverage(String restaurantId) {
        if (restaurantId == null) {
            return;
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null) {
            return;
        }

        if (restaurant.getLocations() != null) {
        for (Location location : restaurant.getLocations()) {
            List<Review> locationReviews = reviewRepository.findByLocationId(location.getId());

            double average = locationReviews.isEmpty()
                    ? 0.0
                    : locationReviews.stream()
                            .mapToInt(Review::getStars)
                            .average()
                            .orElse(0.0);

            location.setAverageRating(average);
        }
    }

    restaurantRepository.save(restaurant);
}
}
