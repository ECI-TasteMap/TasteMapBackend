package com.eci.edu.ieti.tastemap.reservations.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservationDtoTest {

    private LocalDate date;
    private LocalTime time;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        date = LocalDate.of(2026, 5, 20);
        time = LocalTime.of(19, 30);
        now = LocalDateTime.now();
    }

    @Test
    void testReservationRequestDtoCreation() {
        ReservationRequestDto requestDto = new ReservationRequestDto(
            "restaurant1", "location1", date, time, 4, "Window seat preferred"
        );

        assertNotNull(requestDto);
        assertEquals("restaurant1", requestDto.getRestaurantId());
        assertEquals("location1", requestDto.getLocationId());
        assertEquals(date, requestDto.getDate());
        assertEquals(time, requestDto.getTime());
        assertEquals(4, requestDto.getNumberOfGuests());
        assertEquals("Window seat preferred", requestDto.getSpecialRequests());
    }

    @Test
    void testReservationRequestDtoSettersAndGetters() {
        ReservationRequestDto requestDto = new ReservationRequestDto();
        requestDto.setRestaurantId("restaurant2");
        requestDto.setLocationId("location2");
        requestDto.setDate(date);
        requestDto.setTime(time);
        requestDto.setNumberOfGuests(2);
        requestDto.setSpecialRequests("No spicy");

        assertEquals("restaurant2", requestDto.getRestaurantId());
        assertEquals("location2", requestDto.getLocationId());
        assertEquals(2, requestDto.getNumberOfGuests());
        assertEquals("No spicy", requestDto.getSpecialRequests());
    }

    @Test
    void testReservationResponseDtoCreation() {
        ReservationResponseDto responseDto = new ReservationResponseDto(
            "1", "user1", "restaurant1", "location1",
            date, time, 4, "Window seat preferred", now, now
        );

        assertNotNull(responseDto);
        assertEquals("1", responseDto.getId());
        assertEquals("user1", responseDto.getUserId());
        assertEquals("restaurant1", responseDto.getRestaurantId());
        assertEquals("location1", responseDto.getLocationId());
        assertEquals(date, responseDto.getDate());
        assertEquals(time, responseDto.getTime());
        assertEquals(4, responseDto.getNumberOfGuests());
        assertEquals("Window seat preferred", responseDto.getSpecialRequests());
        assertNotNull(responseDto.getCreatedAt());
        assertNotNull(responseDto.getUpdatedAt());
    }

    @Test
    void testReservationResponseDtoSettersAndGetters() {
        ReservationResponseDto responseDto = new ReservationResponseDto();
        responseDto.setId("2");
        responseDto.setUserId("user2");
        responseDto.setRestaurantId("restaurant2");
        responseDto.setLocationId("location2");
        responseDto.setDate(date);
        responseDto.setTime(time);
        responseDto.setNumberOfGuests(6);
        responseDto.setSpecialRequests("Vegetarian options");
        responseDto.setCreatedAt(now);
        responseDto.setUpdatedAt(now);

        assertEquals("2", responseDto.getId());
        assertEquals("user2", responseDto.getUserId());
        assertEquals("restaurant2", responseDto.getRestaurantId());
        assertEquals("location2", responseDto.getLocationId());
        assertEquals(6, responseDto.getNumberOfGuests());
        assertEquals("Vegetarian options", responseDto.getSpecialRequests());
    }

    @Test
    void testReservationRequestDtoWithNullValues() {
        ReservationRequestDto requestDto = new ReservationRequestDto(
            "restaurant1", "location1", date, time, 2, null
        );

        assertNotNull(requestDto);
        assertNull(requestDto.getSpecialRequests());
        assertEquals("location1", requestDto.getLocationId());
        assertEquals(2, requestDto.getNumberOfGuests());
    }

    @Test
    void testReservationResponseDtoWithNullSpecialRequests() {
        ReservationResponseDto responseDto = new ReservationResponseDto(
            "3", "user3", "restaurant1", "location3",
            date, time, 3, null, now, now
        );

        assertNull(responseDto.getSpecialRequests());
        assertEquals("restaurant1", responseDto.getRestaurantId());
        assertEquals("location3", responseDto.getLocationId());
        assertEquals(3, responseDto.getNumberOfGuests());
        assertEquals("user3", responseDto.getUserId());
    }
}