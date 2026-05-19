package com.eci.edu.ieti.tastemap.reservations.service;

import com.eci.edu.ieti.tastemap.reservations.dto.ReservationRequestDto;
import com.eci.edu.ieti.tastemap.reservations.exception.ReservationNotFoundException;
import com.eci.edu.ieti.tastemap.reservations.mapper.ReservationMapper;
import com.eci.edu.ieti.tastemap.reservations.model.Reservation;
import com.eci.edu.ieti.tastemap.reservations.repository.ReservationRepository;
import com.eci.edu.ieti.tastemap.reservations.service.serviceImpl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Reservation reservation;
    private ReservationRequestDto reservationRequestDto;

    @BeforeEach
    void setUp() {
        LocalDate date = LocalDate.of(2026, 5, 20);
        LocalTime time = LocalTime.of(19, 30);
        LocalDateTime now = LocalDateTime.now();

        reservation = new Reservation(
            "1", "user1","location1",
            date, time, 4, "Window seat preferred", now, now
        );

        reservationRequestDto = new ReservationRequestDto(
            "restaurant1", "location1", date, time, 4, "Window seat preferred"
        );
    }

    @Test
    void testCreate() {
        when(reservationMapper.toReservation(reservationRequestDto)).thenReturn(reservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation createdReservation = reservationService.create(reservationRequestDto, "user1");

        assertNotNull(createdReservation);
        assertEquals("user1", createdReservation.getUserId());
        assertEquals("location1", createdReservation.getLocationId());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testFindById() {
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        Optional<Reservation> foundReservation = reservationService.findById("1");

        assertTrue(foundReservation.isPresent());
        assertEquals(reservation, foundReservation.get());
        verify(reservationRepository, times(1)).findById("1");
    }

    @Test
    void testFindByIdNotFound() {
        when(reservationRepository.findById("1")).thenReturn(Optional.empty());

        Optional<Reservation> foundReservation = reservationService.findById("1");

        assertFalse(foundReservation.isPresent());
        verify(reservationRepository, times(1)).findById("1");
    }

    @Test
    void testAll() {
        when(reservationRepository.findAll()).thenReturn(Collections.singletonList(reservation));

        List<Reservation> reservations = reservationService.all();

        assertEquals(1, reservations.size());
        assertEquals(reservation, reservations.get(0));
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testFindByUserId() {
        when(reservationRepository.findByUserId("user1")).thenReturn(Collections.singletonList(reservation));

        List<Reservation> reservations = reservationService.findByUserId("user1");

        assertEquals(1, reservations.size());
        assertEquals(reservation, reservations.get(0));
        verify(reservationRepository, times(1)).findByUserId("user1");
    }

    @Test
    void testFindByRestaurantId() {
        when(reservationRepository.findByLocationId("restaurant1")).thenReturn(Collections.singletonList(reservation));

        List<Reservation> reservations = reservationService.findByRestaurantId("restaurant1");

        assertEquals(1, reservations.size());
        assertEquals(reservation, reservations.get(0));
        verify(reservationRepository, times(1)).findByLocationId("restaurant1");
    }

    @Test
    void testFindByLocationId() {
        when(reservationRepository.findByLocationId("location1")).thenReturn(Collections.singletonList(reservation));

        List<Reservation> reservations = reservationService.findByLocationId("location1");

        assertEquals(1, reservations.size());
        assertEquals(reservation, reservations.get(0));
        verify(reservationRepository, times(1)).findByLocationId("location1");
    }

    @Test
    void testFindByRestaurantIdAndDate() {
        LocalDate date = LocalDate.of(2026, 5, 20);
        when(reservationRepository.findByLocationIdAndDate("restaurant1", date))
            .thenReturn(Collections.singletonList(reservation));

        List<Reservation> reservations = reservationService.findByRestaurantIdAndDate("restaurant1", date);

        assertEquals(1, reservations.size());
        assertEquals(reservation, reservations.get(0));
        verify(reservationRepository, times(1)).findByLocationIdAndDate("restaurant1", date);
    }

    @Test
    void testFindUpcomingReservationsByUserId() {
        when(reservationRepository.findByUserIdAndDateGreaterThanEqual("user1", LocalDate.now()))
            .thenReturn(Collections.singletonList(reservation));

        List<Reservation> reservations = reservationService.findUpcomingReservationsByUserId("user1");

        assertEquals(1, reservations.size());
        assertEquals(reservation, reservations.get(0));
        verify(reservationRepository, times(1)).findByUserIdAndDateGreaterThanEqual("user1", LocalDate.now());
    }

    @Test
    void testUpdate() {
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation updatedReservation = reservationService.update("1", reservationRequestDto);

        assertNotNull(updatedReservation);
        assertEquals("location1", updatedReservation.getLocationId());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testUpdateNotFound() {
        when(reservationRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> reservationService.update("1", reservationRequestDto));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testDeleteById() {
        when(reservationRepository.existsById("1")).thenReturn(true);
        doNothing().when(reservationRepository).deleteById("1");

        reservationService.deleteById("1");

        verify(reservationRepository, times(1)).deleteById("1");
    }

    @Test
    void testDeleteByIdNotFound() {
        when(reservationRepository.existsById("1")).thenReturn(false);

        assertThrows(ReservationNotFoundException.class, () -> reservationService.deleteById("1"));
        verify(reservationRepository, never()).deleteById("1");
    }
}