package com.eci.edu.ieti.tastemap.reservations.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        LocalDate date = LocalDate.of(2026, 5, 20);
        LocalTime time = LocalTime.of(19, 30);
        LocalDateTime now = LocalDateTime.now();

        reservation = new Reservation(
            "1",
            "user1",
            "restaurant1",
            date,
            time,
            4,
            "Window seat preferred",
            now,
            now
        );
    }

    @Test
    void testReservationCreation() {
        assertNotNull(reservation);
        assertEquals("1", reservation.getId());
        assertEquals("user1", reservation.getUserId());
        assertEquals("restaurant1", reservation.getRestaurantId());
        assertEquals(4, reservation.getNumberOfGuests());
        assertEquals("Window seat preferred", reservation.getSpecialRequests());
    }

    @Test
    void testReservationSettersAndGetters() {
        reservation.setNumberOfGuests(6);
        assertEquals(6, reservation.getNumberOfGuests());

        reservation.setSpecialRequests("No onions, please");
        assertEquals("No onions, please", reservation.getSpecialRequests());

        LocalTime newTime = LocalTime.of(20, 0);
        reservation.setTime(newTime);
        assertEquals(newTime, reservation.getTime());

        LocalDate newDate = LocalDate.of(2026, 6, 1);
        reservation.setDate(newDate);
        assertEquals(newDate, reservation.getDate());
    }

    @Test
    void testReservationEquality() {
        LocalDate date = LocalDate.of(2026, 5, 20);
        LocalTime time = LocalTime.of(19, 30);
        LocalDateTime now = LocalDateTime.now();

        Reservation reservation2 = new Reservation(
            "1",
            "user1",
            "restaurant1",
            date,
            time,
            4,
            "Window seat preferred",
            now,
            now
        );

        assertEquals(reservation.getId(), reservation2.getId());
        assertEquals(reservation.getUserId(), reservation2.getUserId());
    }

    @Test
    void testReservationWithNullSpecialRequests() {
        LocalDate date = LocalDate.of(2026, 5, 20);
        LocalTime time = LocalTime.of(19, 30);
        LocalDateTime now = LocalDateTime.now();

        Reservation reservation2 = new Reservation(
            "2",
            "user2",
            "restaurant2",
            date,
            time,
            2,
            null,
            now,
            now
        );

        assertNull(reservation2.getSpecialRequests());
        assertEquals(2, reservation2.getNumberOfGuests());
    }

    @Test
    void testReservationTimestamps() {
        LocalDateTime now = LocalDateTime.now();
        assertNotNull(reservation.getCreatedAt());
        assertNotNull(reservation.getUpdatedAt());
        assertEquals(reservation.getCreatedAt(), reservation.getUpdatedAt());
    }
}
