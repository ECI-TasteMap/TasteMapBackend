package com.eci.edu.ieti.tastemap.reviews.controller;

import com.eci.edu.ieti.tastemap.reviews.dto.ReviewRequestDto;
import com.eci.edu.ieti.tastemap.reviews.dto.ReviewResponseDto;
import com.eci.edu.ieti.tastemap.reviews.dto.ReviewAverageResponseDto;
import com.eci.edu.ieti.tastemap.reviews.mapper.ReviewMapper;
import com.eci.edu.ieti.tastemap.reviews.model.Review;
import com.eci.edu.ieti.tastemap.reviews.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewController reviewController;

    private Review review;
    private ReviewRequestDto reviewRequestDto;
    private ReviewResponseDto reviewResponseDto;

    @BeforeEach
    void setUp() {
        review = new Review("1", "user1", "restaurant1", "location1", "Great!", 5);
        reviewRequestDto = new ReviewRequestDto("user1", "restaurant1", "location1", "Great!", 5);
        reviewResponseDto = new ReviewResponseDto("1", "user1", "restaurant1", "location1", "Great!", 5);
    }

    @Test
    void testCreate() {
        when(reviewService.create(reviewRequestDto)).thenReturn(review);
        when(reviewMapper.toReviewResponseDto(review)).thenReturn(reviewResponseDto);

        ResponseEntity<ReviewResponseDto> response = reviewController.create(reviewRequestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(reviewResponseDto, response.getBody());
        verify(reviewService, times(1)).create(reviewRequestDto);
    }

    @Test
    void testFindById() {
        when(reviewService.findById("1")).thenReturn(Optional.of(review));
        when(reviewMapper.toReviewResponseDto(review)).thenReturn(reviewResponseDto);

        ResponseEntity<ReviewResponseDto> response = reviewController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewResponseDto, response.getBody());
        verify(reviewService, times(1)).findById("1");
    }

    @Test
    void testAll() {
        when(reviewService.all()).thenReturn(Collections.singletonList(review));
        when(reviewMapper.toReviewResponseDto(review)).thenReturn(reviewResponseDto);

        ResponseEntity<List<ReviewResponseDto>> response = reviewController.all();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(reviewResponseDto, response.getBody().get(0));
        verify(reviewService, times(1)).all();
    }

    @Test
    void testFindByRestaurantId() {
        when(reviewService.findByRestaurantId("restaurant1")).thenReturn(Collections.singletonList(review));
        when(reviewMapper.toReviewResponseDto(review)).thenReturn(reviewResponseDto);

        ResponseEntity<List<ReviewResponseDto>> response = reviewController.findByRestaurantId("restaurant1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(reviewResponseDto, response.getBody().get(0));
        verify(reviewService, times(1)).findByRestaurantId("restaurant1");
    }

    @Test
    void testFindByUserId() {
        when(reviewService.findByUserId("user1")).thenReturn(Collections.singletonList(review));
        when(reviewMapper.toReviewResponseDto(review)).thenReturn(reviewResponseDto);

        ResponseEntity<List<ReviewResponseDto>> response = reviewController.findByUserId("user1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(reviewResponseDto, response.getBody().get(0));
        verify(reviewService, times(1)).findByUserId("user1");
    }

    @Test
    void testFindByLocationId() {
        when(reviewService.findByLocationId("location1")).thenReturn(Collections.singletonList(review));
        when(reviewMapper.toReviewResponseDto(review)).thenReturn(reviewResponseDto);

        ResponseEntity<List<ReviewResponseDto>> response = reviewController.findByLocationId("location1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(reviewResponseDto, response.getBody().get(0));
        verify(reviewService, times(1)).findByLocationId("location1");
    }

    @Test
    void testAverageByRestaurantId() {
        when(reviewService.averageByRestaurantId("restaurant1")).thenReturn(4.0);

        ResponseEntity<ReviewAverageResponseDto> response = reviewController.averageByRestaurantId("restaurant1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("restaurant1", response.getBody().getRestaurantId());
        assertEquals(4.0, response.getBody().getAverage());
        verify(reviewService, times(1)).averageByRestaurantId("restaurant1");
    }

    @Test
    void testUpdate() {
        when(reviewService.update("1", reviewRequestDto)).thenReturn(review);
        when(reviewMapper.toReviewResponseDto(review)).thenReturn(reviewResponseDto);

        ResponseEntity<ReviewResponseDto> response = reviewController.update("1", reviewRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewResponseDto, response.getBody());
        verify(reviewService, times(1)).update("1", reviewRequestDto);
    }

    @Test
    void testDeleteById() {
        doNothing().when(reviewService).deleteById("1");

        ResponseEntity<Void> response = reviewController.deleteById("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(reviewService, times(1)).deleteById("1");
    }
}
