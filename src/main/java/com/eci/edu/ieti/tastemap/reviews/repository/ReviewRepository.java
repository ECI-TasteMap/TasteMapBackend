package com.eci.edu.ieti.tastemap.reviews.repository;

import com.eci.edu.ieti.tastemap.reviews.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Review documents in MongoDB.
 */
@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
	List<Review> findByRestaurantId(String restaurantId);
}

