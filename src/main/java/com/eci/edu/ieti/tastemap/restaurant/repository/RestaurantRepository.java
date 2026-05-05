package com.eci.edu.ieti.tastemap.restaurant.repository;

import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Restaurant documents in MongoDB.
 */
@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
}

