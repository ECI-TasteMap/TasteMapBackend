package com.eci.edu.ieti.tastemap.reservations.service;

import com.eci.edu.ieti.tastemap.reservations.dto.ReservationRequestDto;
import com.eci.edu.ieti.tastemap.reservations.model.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for reservation-related operations.
 */
public interface ReservationService {
    
    /**
     * Create a new reservation.
     *
     * @param reservationRequestDto the reservation data
     * @return the created reservation
     */
    Reservation create(ReservationRequestDto reservationRequestDto, String userId);
    
    /**
     * Find a reservation by ID.
     *
     * @param id the reservation ID
     * @return an Optional containing the reservation if found
     */
    Optional<Reservation> findById(String id);
    
    /**
     * Get all reservations.
     *
     * @return a list of all reservations
     */
    List<Reservation> all();
    
    /**
     * Get all reservations for a specific user.
     *
     * @param userId the user ID
     * @return a list of reservations for the user
     */
    List<Reservation> findByUserId(String userId);
    
    /**
     * Get all reservations for a specific restaurant.
     *
     * @param restaurantId the restaurant ID
     * @return a list of reservations for the restaurant
     */
    List<Reservation> findByRestaurantId(String restaurantId);
    
    /**
     * Get all reservations for a specific restaurant on a specific date.
     *
     * @param restaurantId the restaurant ID
     * @param date the date to filter by
     * @return a list of reservations for the restaurant on the given date
     */
    List<Reservation> findByRestaurantIdAndDate(String restaurantId, LocalDate date);
    
    /**
     * Get all upcoming reservations for a specific user.
     *
     * @param userId the user ID
     * @return a list of upcoming reservations for the user
     */
    List<Reservation> findUpcomingReservationsByUserId(String userId);
    
    /**
     * Update an existing reservation.
     *
     * @param id the reservation ID
     * @param reservationRequestDto the updated reservation data
     * @return the updated reservation
     */
    Reservation update(String id, ReservationRequestDto reservationRequestDto);
    
    /**
     * Delete a reservation by ID.
     *
     * @param id the reservation ID
     */
    void deleteById(String id);

    /**
     * Mark a reservation as accepted by the restaurant.
     *
     * @param id the reservation ID
     * @return the updated reservation
     */
    Reservation acceptReservation(String id);

    /**
     * Mark a reservation as denied by the restaurant.
     *
     * @param id the reservation ID
     * @return the updated reservation
     */
    Reservation denyReservation(String id);

    /**
     * Mark a reservation as canceled.
     *
     * @param id the reservation ID
     * @return the updated reservation
     */
    Reservation cancelReservation(String id);

    List<Reservation> findByLocationId(String locationId);
}
