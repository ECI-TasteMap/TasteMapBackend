package com.eci.edu.ieti.tastemap.reservations.controller;

import com.eci.edu.ieti.tastemap.reservations.dto.ReservationRequestDto;
import com.eci.edu.ieti.tastemap.reservations.dto.ReservationResponseDto;
import com.eci.edu.ieti.tastemap.reservations.exception.ReservationNotFoundException;
import com.eci.edu.ieti.tastemap.reservations.mapper.ReservationMapper;
import com.eci.edu.ieti.tastemap.reservations.model.Reservation;
import com.eci.edu.ieti.tastemap.reservations.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing reservations.
 */
@RestController
@RequestMapping("/api/v1/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {
    
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    
    public ReservationController(ReservationService reservationService, ReservationMapper reservationMapper) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
    }
    
    /**
     * Create a new reservation.
     *
     * @param reservationRequestDto the reservation data
     * @return the created reservation
     */
    @PostMapping
    public ResponseEntity<ReservationResponseDto> create(@RequestBody ReservationRequestDto reservationRequestDto) {
        Reservation reservation = reservationService.create(reservationRequestDto);
        ReservationResponseDto reservationResponseDto = reservationMapper.toReservationResponseDto(reservation);
        return ResponseEntity.created(URI.create("/api/v1/reservations/" + reservation.getId())).body(reservationResponseDto);
    }
    
    /**
     * Get a reservation by ID.
     *
     * @param id the reservation ID
     * @return the reservation
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> findById(@PathVariable String id) {
        Reservation reservation = reservationService.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation with id " + id + " not found"));
        ReservationResponseDto reservationResponseDto = reservationMapper.toReservationResponseDto(reservation);
        return ResponseEntity.ok(reservationResponseDto);
    }
    
    /**
     * Get all reservations.
     *
     * @return a list of all reservations
     */
    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> all() {
        List<Reservation> reservations = reservationService.all();
        List<ReservationResponseDto> reservationResponseDtos = reservations.stream()
                .map(reservationMapper::toReservationResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservationResponseDtos);
    }
    
    /**
     * Get all reservations for a specific user.
     *
     * @param userId the user ID
     * @return a list of reservations for the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponseDto>> findByUserId(@PathVariable String userId) {
        List<Reservation> reservations = reservationService.findByUserId(userId);
        List<ReservationResponseDto> reservationResponseDtos = reservations.stream()
                .map(reservationMapper::toReservationResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservationResponseDtos);
    }
    
    /**
     * Get all upcoming reservations for a specific user.
     *
     * @param userId the user ID
     * @return a list of upcoming reservations for the user
     */
    @GetMapping("/user/{userId}/upcoming")
    public ResponseEntity<List<ReservationResponseDto>> findUpcomingByUserId(@PathVariable String userId) {
        List<Reservation> reservations = reservationService.findUpcomingReservationsByUserId(userId);
        List<ReservationResponseDto> reservationResponseDtos = reservations.stream()
                .map(reservationMapper::toReservationResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservationResponseDtos);
    }
    
    /**
     * Get all reservations for a specific restaurant.
     *
     * @param restaurantId the restaurant ID
     * @return a list of reservations for the restaurant
     */
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<ReservationResponseDto>> findByRestaurantId(@PathVariable String restaurantId) {
        List<Reservation> reservations = reservationService.findByRestaurantId(restaurantId);
        List<ReservationResponseDto> reservationResponseDtos = reservations.stream()
                .map(reservationMapper::toReservationResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservationResponseDtos);
    }
    
    /**
     * Get all reservations for a specific restaurant on a specific date.
     *
     * @param restaurantId the restaurant ID
     * @param date the date in YYYY-MM-DD format
     * @return a list of reservations for the restaurant on the given date
     */
    @GetMapping("/restaurant/{restaurantId}/date/{date}")
    public ResponseEntity<List<ReservationResponseDto>> findByRestaurantIdAndDate(
            @PathVariable String restaurantId,
            @PathVariable String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        List<Reservation> reservations = reservationService.findByRestaurantIdAndDate(restaurantId, parsedDate);
        List<ReservationResponseDto> reservationResponseDtos = reservations.stream()
                .map(reservationMapper::toReservationResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservationResponseDtos);
    }
    
    /**
     * Update an existing reservation.
     *
     * @param id the reservation ID
     * @param reservationRequestDto the updated reservation data
     * @return the updated reservation
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> update(
            @PathVariable String id,
            @RequestBody ReservationRequestDto reservationRequestDto) {
        Reservation reservation = reservationService.update(id, reservationRequestDto);
        ReservationResponseDto reservationResponseDto = reservationMapper.toReservationResponseDto(reservation);
        return ResponseEntity.ok(reservationResponseDto);
    }
    
    /**
     * Delete a reservation by ID.
     *
     * @param id the reservation ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
