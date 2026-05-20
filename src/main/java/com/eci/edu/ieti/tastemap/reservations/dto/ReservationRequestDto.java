package com.eci.edu.ieti.tastemap.reservations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data Transfer Object for reservation creation and update requests.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestDto {
    private String restaurantId;
    private String locationId;      
    private LocalDate date;
    private LocalTime time;
    private Integer numberOfGuests;
    private String specialRequests;
    private String userId;

    public ReservationRequestDto(String restaurantId, String locationId, LocalDate date, LocalTime time,
                                 Integer numberOfGuests, String specialRequests) {
        this.restaurantId = restaurantId;
        this.locationId = locationId;
        this.date = date;
        this.time = time;
        this.numberOfGuests = numberOfGuests;
        this.specialRequests = specialRequests;
    }
}
