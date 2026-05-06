package com.eci.edu.ieti.tastemap.reviews.service.serviceImpl;

import com.eci.edu.ieti.tastemap.reviews.dto.ReviewRequestDto;
import com.eci.edu.ieti.tastemap.reviews.exception.ReviewNotFoundException;
import com.eci.edu.ieti.tastemap.reviews.mapper.ReviewMapper;
import com.eci.edu.ieti.tastemap.reviews.model.Review;
import com.eci.edu.ieti.tastemap.reviews.repository.ReviewRepository;
import com.eci.edu.ieti.tastemap.reviews.service.ReviewService;
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

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public Review create(ReviewRequestDto reviewRequestDto) {
        Review review = reviewMapper.toReview(reviewRequestDto);
        return reviewRepository.save(review);
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
        if (!reviewRepository.existsById(id)) {
            throw new ReviewNotFoundException("Review with id " + id + " not found");
        }
        reviewRepository.deleteById(id);
    }

    @Override
    public Review update(String id, ReviewRequestDto reviewRequestDto) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ReviewNotFoundException("Review with id " + id + " not found"));
        review.setUserId(reviewRequestDto.getUserId());
        review.setRestaurantId(reviewRequestDto.getRestaurantId());
        review.setComment(reviewRequestDto.getComment());
        review.setStars(reviewRequestDto.getStars());
        return reviewRepository.save(review);
    }
}

