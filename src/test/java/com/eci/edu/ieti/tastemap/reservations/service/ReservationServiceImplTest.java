package com.eci.edu.ieti.tastemap.reservations.service;

import com.eci.edu.ieti.tastemap.reservations.dto.ReservationRequestDto;
import com.eci.edu.ieti.tastemap.reservations.exception.ReservationNotFoundException;
import com.eci.edu.ieti.tastemap.reservations.mapper.ReservationMapper;
import com.eci.edu.ieti.tastemap.reservations.model.Reservation;
import com.eci.edu.ieti.tastemap.reservations.repository.ReservationRepository;
import com.eci.edu.ieti.tastemap.reservations.service.serviceImpl.ReservationServiceImpl;
import com.eci.edu.ieti.tastemap.restaurant.model.Location;
import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;
import com.eci.edu.ieti.tastemap.restaurant.model.Schedule;
import com.eci.edu.ieti.tastemap.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Reservation reservation;
    private ReservationRequestDto reservationRequestDto;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        LocalDate date = LocalDate.of(2026, 7, 20);
        LocalTime time = LocalTime.of(19, 30);
        LocalDateTime now = LocalDateTime.now();

        reservation = new Reservation(
            "1", "user1", "restaurant1", "location1",
            date, time, 4, "Window seat preferred", now, now
        );

        reservationRequestDto = new ReservationRequestDto(
            "restaurant1", "location1", date, time, 4, "Window seat preferred"
        );

        restaurant = new Restaurant();
        restaurant.setId("restaurant1");
        Location location = new Location();
        location.setId("location1");
        Schedule schedule = new Schedule();
        Set<DayOfWeek> days = new HashSet<>();
        days.add(date.getDayOfWeek());
        schedule.setDays(days);
        schedule.setOpenTime("10:00");
        schedule.setCloseTime("22:00");
        location.setSchedules(Collections.singletonList(schedule));
        restaurant.setLocations(Collections.singletonList(location));
    }

    @Test
    void testCreate() {
        when(restaurantRepository.findById("restaurant1")).thenReturn(Optional.of(restaurant));
        when(reservationMapper.toReservation(reservationRequestDto)).thenReturn(reservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation createdReservation = reservationService.create(reservationRequestDto, "user1");

        assertNotNull(createdReservation);
        assertEquals("user1", createdReservation.getUserId());
        assertEquals("restaurant1", createdReservation.getRestaurantId());
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
        when(reservationRepository.findByRestaurantId("restaurant1")).thenReturn(Collections.singletonList(reservation));

        List<Reservation> reservations = reservationService.findByRestaurantId("restaurant1");

        assertEquals(1, reservations.size());
        assertEquals(reservation, reservations.get(0));
        verify(reservationRepository, times(1)).findByRestaurantId("restaurant1");
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
        LocalDate date = LocalDate.of(2026, 7, 20);
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
        when(restaurantRepository.findById("restaurant1")).thenReturn(Optional.of(restaurant));
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation updatedReservation = reservationService.update("1", reservationRequestDto);

        assertNotNull(updatedReservation);
        assertEquals("restaurant1", updatedReservation.getRestaurantId());
        assertEquals("location1", updatedReservation.getLocationId());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testUpdateReservationNotFound() {

        when(restaurantRepository.findById("restaurant1")).thenReturn(Optional.of(restaurant));
        when(reservationRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> reservationService.update("1", reservationRequestDto));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testUpdateRestaurantNotFound() {
        when(restaurantRepository.findById("restaurant1")).thenReturn(Optional.empty());

        assertThrows(com.eci.edu.ieti.tastemap.restaurant.exception.RestaurantNotFoundException.class, () -> reservationService.update("1", reservationRequestDto));
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