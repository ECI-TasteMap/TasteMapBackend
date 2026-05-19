package com.eci.edu.ieti.tastemap.reservations.controller;

import com.eci.edu.ieti.tastemap.reservations.dto.ReservationRequestDto;
import com.eci.edu.ieti.tastemap.reservations.dto.ReservationResponseDto;
import com.eci.edu.ieti.tastemap.reservations.exception.ReservationNotFoundException;
import com.eci.edu.ieti.tastemap.reservations.mapper.ReservationMapper;
import com.eci.edu.ieti.tastemap.reservations.model.Reservation;
import com.eci.edu.ieti.tastemap.reservations.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationController reservationController;

    private Reservation reservation;
    private ReservationRequestDto reservationRequestDto;
    private ReservationResponseDto reservationResponseDto;
    private JwtAuthenticationToken auth;

    @BeforeEach
    void setUp() {
        LocalDate date = LocalDate.of(2026, 5, 20);
        LocalTime time = LocalTime.of(19, 30);
        LocalDateTime now = LocalDateTime.now();

        reservation = new Reservation(
            "1", "user1", "location1",
            date, time, 4, "Window seat preferred", now, now
        );

        reservationRequestDto = new ReservationRequestDto(
            "restaurant1", "location1", date, time, 4, "Window seat preferred"
        );

        reservationResponseDto = new ReservationResponseDto(
            "1", "user1", "location1",
            date, time, 4, "Window seat preferred", now, now
        );

        // Mock JWT token
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("user1");
        auth = new JwtAuthenticationToken(jwt);
    }

    @Test
    void testCreate() {
        when(reservationService.create(reservationRequestDto, "user1")).thenReturn(reservation);
        when(reservationMapper.toReservationResponseDto(reservation)).thenReturn(reservationResponseDto);

        ResponseEntity<ReservationResponseDto> response = reservationController.create(reservationRequestDto, auth);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(reservationResponseDto, response.getBody());
        verify(reservationService, times(1)).create(reservationRequestDto, "user1");
    }

    @Test
    void testFindById() {
        when(reservationService.findById("1")).thenReturn(Optional.of(reservation));
        when(reservationMapper.toReservationResponseDto(reservation)).thenReturn(reservationResponseDto);

        ResponseEntity<ReservationResponseDto> response = reservationController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservationResponseDto, response.getBody());
        verify(reservationService, times(1)).findById("1");
    }

    @Test
    void testFindByIdNotFound() {
        when(reservationService.findById("1")).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> reservationController.findById("1"));
        verify(reservationService, times(1)).findById("1");
    }

    private void assertThrows(Class<? extends Throwable> expectedType, org.junit.jupiter.api.function.Executable executable) {
        org.junit.jupiter.api.Assertions.assertThrows(expectedType, executable);
    }

    @Test
    void testAll() {
        when(reservationService.all()).thenReturn(Collections.singletonList(reservation));
        when(reservationMapper.toReservationResponseDto(reservation)).thenReturn(reservationResponseDto);

        ResponseEntity<List<ReservationResponseDto>> response = reservationController.all();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(reservationService, times(1)).all();
    }

    @Test
    void testFindByUserId() {
        when(reservationService.findByUserId("user1")).thenReturn(Collections.singletonList(reservation));
        when(reservationMapper.toReservationResponseDto(reservation)).thenReturn(reservationResponseDto);

        ResponseEntity<List<ReservationResponseDto>> response = reservationController.findByUserId("user1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(reservationService, times(1)).findByUserId("user1");
    }

    @Test
    void testFindUpcomingByUserId() {
        when(reservationService.findUpcomingReservationsByUserId("user1")).thenReturn(Collections.singletonList(reservation));
        when(reservationMapper.toReservationResponseDto(reservation)).thenReturn(reservationResponseDto);

        ResponseEntity<List<ReservationResponseDto>> response = reservationController.findUpcomingByUserId("user1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(reservationService, times(1)).findUpcomingReservationsByUserId("user1");
    }

    @Test
    void testFindByRestaurantId() {
        when(reservationService.findByRestaurantId("restaurant1")).thenReturn(Collections.singletonList(reservation));
        when(reservationMapper.toReservationResponseDto(reservation)).thenReturn(reservationResponseDto);

        ResponseEntity<List<ReservationResponseDto>> response = reservationController.findByRestaurantId("restaurant1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(reservationService, times(1)).findByRestaurantId("restaurant1");
    }

    @Test
    void testFindByLocationId() {
        when(reservationService.findByLocationId("location1")).thenReturn(Collections.singletonList(reservation));
        when(reservationMapper.toReservationResponseDto(reservation)).thenReturn(reservationResponseDto);

        ResponseEntity<List<ReservationResponseDto>> response = reservationController.findByLocationId("location1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(reservationService, times(1)).findByLocationId("location1");
    }

    @Test
    void testFindByRestaurantIdAndDate() {
        LocalDate date = LocalDate.of(2026, 5, 20);
        when(reservationService.findByRestaurantIdAndDate("restaurant1", date))
            .thenReturn(Collections.singletonList(reservation));
        when(reservationMapper.toReservationResponseDto(reservation)).thenReturn(reservationResponseDto);

        ResponseEntity<List<ReservationResponseDto>> response = reservationController.findByRestaurantIdAndDate("restaurant1", "2026-05-20");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(reservationService, times(1)).findByRestaurantIdAndDate("restaurant1", date);
    }

    @Test
    void testUpdate() {
        when(reservationService.update("1", reservationRequestDto)).thenReturn(reservation);
        when(reservationMapper.toReservationResponseDto(reservation)).thenReturn(reservationResponseDto);

        ResponseEntity<ReservationResponseDto> response = reservationController.update("1", reservationRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservationResponseDto, response.getBody());
        verify(reservationService, times(1)).update("1", reservationRequestDto);
    }

    @Test
    void testDeleteById() {
        doNothing().when(reservationService).deleteById("1");

        ResponseEntity<Void> response = reservationController.deleteById("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(reservationService, times(1)).deleteById("1");
    }
}