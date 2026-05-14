package com.eci.edu.ieti.tastemap.reviews.service.serviceImpl;

import com.eci.edu.ieti.tastemap.reviews.dto.ReviewRequestDto;
import com.eci.edu.ieti.tastemap.reviews.exception.ReviewNotFoundException;
import com.eci.edu.ieti.tastemap.reviews.mapper.ReviewMapper;
import com.eci.edu.ieti.tastemap.reviews.model.Review;
import com.eci.edu.ieti.tastemap.reviews.repository.ReviewRepository;
import com.eci.edu.ieti.tastemap.reviews.service.ReviewService;
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

        List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);
        double average = reviews.isEmpty()
                ? 0.0
                : reviews.stream().mapToInt(Review::getStars).average().orElse(0.0);

        restaurant.setAverageRating(average);
        restaurantRepository.save(restaurant);
    }
}

