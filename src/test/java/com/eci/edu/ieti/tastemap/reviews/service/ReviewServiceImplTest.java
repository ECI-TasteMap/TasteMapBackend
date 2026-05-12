package com.eci.edu.ieti.tastemap.reviews.service;

import com.eci.edu.ieti.tastemap.reviews.dto.ReviewRequestDto;
import com.eci.edu.ieti.tastemap.reviews.exception.ReviewNotFoundException;
import com.eci.edu.ieti.tastemap.reviews.mapper.ReviewMapper;
import com.eci.edu.ieti.tastemap.reviews.model.Review;
import com.eci.edu.ieti.tastemap.reviews.repository.ReviewRepository;
import com.eci.edu.ieti.tastemap.reviews.service.serviceImpl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Review review;
    private ReviewRequestDto reviewRequestDto;

    @BeforeEach
    void setUp() {
        review = new Review("1", "user1", "restaurant1", "Great!", 5);
        reviewRequestDto = new ReviewRequestDto("user1", "restaurant1", "Great!", 5);
    }

    @Test
    void testCreate() {
        when(reviewMapper.toReview(reviewRequestDto)).thenReturn(review);
        when(reviewRepository.save(review)).thenReturn(review);

        Review createdReview = reviewService.create(reviewRequestDto);

        assertEquals(review, createdReview);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testFindById() {
        when(reviewRepository.findById("1")).thenReturn(Optional.of(review));

        Optional<Review> foundReview = reviewService.findById("1");

        assertTrue(foundReview.isPresent());
        assertEquals(review, foundReview.get());
        verify(reviewRepository, times(1)).findById("1");
    }

    @Test
    void testAll() {
        when(reviewRepository.findAll()).thenReturn(Collections.singletonList(review));

        List<Review> reviews = reviewService.all();

        assertEquals(1, reviews.size());
        assertEquals(review, reviews.get(0));
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    void testAverageByRestaurantId() {
        List<Review> reviews = List.of(
                new Review("1", "user1", "restaurant1", "Good", 5),
                new Review("2", "user2", "restaurant1", "Ok", 3),
                new Review("3", "user3", "restaurant1", "Great", 4)
        );
        when(reviewRepository.findByRestaurantId("restaurant1")).thenReturn(reviews);

        double average = reviewService.averageByRestaurantId("restaurant1");

        assertEquals(4.0, average);
        verify(reviewRepository, times(1)).findByRestaurantId("restaurant1");
    }

    @Test
    void testAverageByRestaurantIdNoReviews() {
        when(reviewRepository.findByRestaurantId("restaurant1")).thenReturn(Collections.emptyList());

        double average = reviewService.averageByRestaurantId("restaurant1");

        assertEquals(0.0, average);
        verify(reviewRepository, times(1)).findByRestaurantId("restaurant1");
    }

    @Test
    void testDeleteById() {
        when(reviewRepository.existsById("1")).thenReturn(true);
        doNothing().when(reviewRepository).deleteById("1");

        reviewService.deleteById("1");

        verify(reviewRepository, times(1)).deleteById("1");
    }

    @Test
    void testDeleteByIdNotFound() {
        when(reviewRepository.existsById("1")).thenReturn(false);

        assertThrows(ReviewNotFoundException.class, () -> reviewService.deleteById("1"));
        verify(reviewRepository, never()).deleteById("1");
    }

    @Test
    void testUpdate() {
        when(reviewRepository.findById("1")).thenReturn(Optional.of(review));
        when(reviewRepository.save(review)).thenReturn(review);

        Review updatedReview = reviewService.update("1", reviewRequestDto);

        assertEquals(review, updatedReview);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testUpdateNotFound() {
        when(reviewRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.update("1", reviewRequestDto));
        verify(reviewRepository, never()).save(any(Review.class));
    }
}

