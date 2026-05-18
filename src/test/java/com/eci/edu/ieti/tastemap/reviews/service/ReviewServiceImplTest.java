package com.eci.edu.ieti.tastemap.reviews.service;

import com.eci.edu.ieti.tastemap.restaurant.model.Location;
import com.eci.edu.ieti.tastemap.restaurant.model.Schedule;
import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;
import com.eci.edu.ieti.tastemap.restaurant.repository.RestaurantRepository;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private RestaurantRepository restaurantRepository;

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
        String restaurantId = "restaurant1";
        Review reviewBeforeSave = new Review(null, "user1", restaurantId, "Great!", 5);
        reviewBeforeSave.setId("1");
        
        Restaurant restaurant = buildRestaurant(restaurantId);
        
        when(reviewMapper.toReview(reviewRequestDto)).thenReturn(reviewBeforeSave);
        when(reviewRepository.save(reviewBeforeSave)).thenReturn(review);
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(reviewRepository.findByRestaurantId(restaurantId)).thenReturn(List.of(review));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        Review createdReview = reviewService.create(reviewRequestDto);

        assertEquals(review, createdReview);
        verify(reviewRepository, times(1)).save(reviewBeforeSave);
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
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
    void testFindByRestaurantId() {
        when(reviewRepository.findByRestaurantId("restaurant1")).thenReturn(Collections.singletonList(review));

        List<Review> reviews = reviewService.findByRestaurantId("restaurant1");

        assertEquals(1, reviews.size());
        assertEquals(review, reviews.get(0));
        verify(reviewRepository, times(1)).findByRestaurantId("restaurant1");
    }

    @Test
    void testFindByUserId() {
        when(reviewRepository.findByUserId("user1")).thenReturn(Collections.singletonList(review));

        List<Review> reviews = reviewService.findByUserId("user1");

        assertEquals(1, reviews.size());
        assertEquals(review, reviews.get(0));
        verify(reviewRepository, times(1)).findByUserId("user1");
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
        String restaurantId = "restaurant1";
        review.setRestaurantId(restaurantId);
        
        Restaurant restaurant = buildRestaurant(restaurantId);
        
        when(reviewRepository.findById("1")).thenReturn(Optional.of(review));
        doNothing().when(reviewRepository).deleteById("1");
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(reviewRepository.findByRestaurantId(restaurantId)).thenReturn(Collections.emptyList());
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        reviewService.deleteById("1");

        verify(reviewRepository, times(1)).deleteById("1");
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void testDeleteByIdNotFound() {
        when(reviewRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.deleteById("1"));
        verify(reviewRepository, never()).deleteById("1");
    }

    @Test
    void testUpdate() {
        String restaurantId = "restaurant1";
        review.setRestaurantId(restaurantId);
        
        Restaurant restaurant = buildRestaurant(restaurantId);
        
        when(reviewRepository.findById("1")).thenReturn(Optional.of(review));
        when(reviewRepository.save(review)).thenReturn(review);
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(reviewRepository.findByRestaurantId(restaurantId)).thenReturn(List.of(review));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        Review updatedReview = reviewService.update("1", reviewRequestDto);

        assertEquals(review, updatedReview);
        verify(reviewRepository, times(1)).save(review);
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void testUpdateNotFound() {
        when(reviewRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.update("1", reviewRequestDto));
        verify(reviewRepository, never()).save(any(Review.class));
    }

    private Restaurant buildRestaurant(String restaurantId) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setOwnerId("owner1");
        restaurant.setName("Test Restaurant");
        restaurant.setDescription("Description");
        restaurant.setLogo("logo.png");
        restaurant.setMenu("menu.pdf");
        restaurant.setTheme(List.of("Theme"));
        restaurant.setLocations(List.of(buildLocation()));
        restaurant.setTags(Set.of("Italian"));
        restaurant.setPriceMin(10);
        restaurant.setPriceMax(30);
        return restaurant;
    }

    private Location buildLocation() {
        DayOfWeek today = LocalDate.now(ZoneId.of("America/Bogota")).getDayOfWeek();
        Schedule schedule = new Schedule(Set.of(today), "00:00", "23:59", false);
        return new Location("loc-1", "North", "3001234567", 4.5, List.of(schedule));
    }
}

