package com.eci.edu.ieti.tastemap.reviews.controller;

import com.eci.edu.ieti.tastemap.reviews.dto.ReviewRequestDto;
import com.eci.edu.ieti.tastemap.reviews.dto.ReviewResponseDto;
import com.eci.edu.ieti.tastemap.reviews.dto.ReviewAverageResponseDto;
import com.eci.edu.ieti.tastemap.reviews.exception.ReviewNotFoundException;
import com.eci.edu.ieti.tastemap.reviews.mapper.ReviewMapper;
import com.eci.edu.ieti.tastemap.reviews.model.Review;
import com.eci.edu.ieti.tastemap.reviews.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing reviews.
 */
@RestController
@RequestMapping("/api/v1/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    public ReviewController(ReviewService reviewService, ReviewMapper reviewMapper) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDto> create(@RequestBody ReviewRequestDto reviewRequestDto) {
        Review review = reviewService.create(reviewRequestDto);
        ReviewResponseDto reviewResponseDto = reviewMapper.toReviewResponseDto(review);
        return ResponseEntity.created(URI.create("/api/v1/reviews/" + review.getId())).body(reviewResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> findById(@PathVariable String id) {
        Review review = reviewService.findById(id).orElseThrow(() -> new ReviewNotFoundException("Review with id " + id + " not found"));
        ReviewResponseDto reviewResponseDto = reviewMapper.toReviewResponseDto(review);
        return ResponseEntity.ok(reviewResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> all() {
        List<Review> reviews = reviewService.all();
        List<ReviewResponseDto> reviewResponseDtos = reviews.stream()
                .map(reviewMapper::toReviewResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviewResponseDtos);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<ReviewResponseDto>> findByRestaurantId(@PathVariable String restaurantId) {
        List<Review> reviews = reviewService.findByRestaurantId(restaurantId);
        List<ReviewResponseDto> reviewResponseDtos = reviews.stream()
                .map(reviewMapper::toReviewResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviewResponseDtos);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponseDto>> findByUserId(@PathVariable String userId) {
        List<Review> reviews = reviewService.findByUserId(userId);
        List<ReviewResponseDto> reviewResponseDtos = reviews.stream()
                .map(reviewMapper::toReviewResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviewResponseDtos);
    }

    @GetMapping("/restaurant/{restaurantId}/average")
    public ResponseEntity<ReviewAverageResponseDto> averageByRestaurantId(@PathVariable String restaurantId) {
        double average = reviewService.averageByRestaurantId(restaurantId);
        return ResponseEntity.ok(new ReviewAverageResponseDto(restaurantId, average));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> update(@PathVariable String id, @RequestBody ReviewRequestDto reviewRequestDto) {
        Review review = reviewService.update(id, reviewRequestDto);
        ReviewResponseDto reviewResponseDto = reviewMapper.toReviewResponseDto(review);
        return ResponseEntity.ok(reviewResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        reviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

