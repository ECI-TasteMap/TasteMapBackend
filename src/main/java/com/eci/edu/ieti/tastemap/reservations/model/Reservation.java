package com.eci.edu.ieti.tastemap.reservations.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import com.eci.edu.ieti.tastemap.reservations.model.ReservationStatus;

/**
 * Represents a reservation for a restaurant.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reservations")
public class Reservation {

    @Id
    private String id;
    private String userId;
    private String restaurantId;
    private String locationId;
    private LocalDate date;
    private LocalTime time;
    private Integer numberOfGuests;
    private String specialRequests;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ReservationStatus status = ReservationStatus.PENDING;

    /**
     * Backwards-compatible constructor used in tests and other places prior to adding `status`.
     */
    public Reservation(String id, String userId, String restaurantId, String locationId,
                       LocalDate date, LocalTime time, Integer numberOfGuests, String specialRequests,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.locationId = locationId;
        this.date = date;
        this.time = time;
        this.numberOfGuests = numberOfGuests;
        this.specialRequests = specialRequests;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = ReservationStatus.PENDING;
    }
}
