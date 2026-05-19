package com.eci.edu.ieti.tastemap.reservations.repository;

import com.eci.edu.ieti.tastemap.reservations.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Reservation documents in MongoDB.
 */
@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {

    /**
     * Find all reservations for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of reservations for the user
     */
    List<Reservation> findByUserId(String userId);

    /**
     * Find all reservations for a specific location.
     *
     * @param locationId the ID of the location
     * @return a list of reservations for the location
     */
    List<Reservation> findByLocationId(String locationId);

    /**
     * Find all reservations for a specific location on a specific date.
     *
     * @param locationId the ID of the location
     * @param date the date of the reservations
     * @return a list of reservations for the location on the given date
     */
    List<Reservation> findByLocationIdAndDate(String locationId, LocalDate date);

    /**
     * Find all reservations for a specific user on a specific date or later.
     *
     * @param userId the ID of the user
     * @param date the date to filter from
     * @return a list of upcoming reservations for the user
     */
    List<Reservation> findByUserIdAndDateGreaterThanEqual(String userId, LocalDate date);
}