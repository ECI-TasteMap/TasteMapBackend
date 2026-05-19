package com.eci.edu.ieti.tastemap.reservations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for reservation data in responses.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDto {
    private String id;
    private String userId;
    private String locationId;      
    private LocalDate date;
    private LocalTime time;
    private Integer numberOfGuests;
    private String specialRequests;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
