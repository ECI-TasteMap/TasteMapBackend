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
}
