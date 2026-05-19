package com.eci.edu.ieti.tastemap.reservations.service.serviceImpl;

import com.eci.edu.ieti.tastemap.reservations.dto.ReservationRequestDto;
import com.eci.edu.ieti.tastemap.reservations.exception.ReservationNotFoundException;
import com.eci.edu.ieti.tastemap.reservations.mapper.ReservationMapper;
import com.eci.edu.ieti.tastemap.reservations.model.Reservation;
import com.eci.edu.ieti.tastemap.reservations.repository.ReservationRepository;
import com.eci.edu.ieti.tastemap.reservations.service.ReservationService;
import com.eci.edu.ieti.tastemap.restaurant.exception.RestaurantNotFoundException;
import com.eci.edu.ieti.tastemap.restaurant.model.Restaurant;
import com.eci.edu.ieti.tastemap.restaurant.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the ReservationService interface.
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final RestaurantRepository restaurantRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationMapper reservationMapper, RestaurantRepository restaurantRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Reservation create(ReservationRequestDto dto, String userId) {
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + dto.getRestaurantId() + " not found"));

        boolean locationExists = restaurant.getLocations().stream()
                .anyMatch(location -> location.getId().equals(dto.getLocationId()));

        if (!locationExists) {
            throw new RestaurantNotFoundException("Location with id " + dto.getLocationId() + " not found in restaurant " + dto.getRestaurantId());
        }

        Reservation reservation = reservationMapper.toReservation(dto);
        reservation.setUserId(userId); // viene del JWT
        reservation.setRestaurantId(dto.getRestaurantId());
        reservation.setLocationId(dto.getLocationId());
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation update(String id, ReservationRequestDto dto) {
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + dto.getRestaurantId() + " not found"));

        boolean locationExists = restaurant.getLocations().stream()
                .anyMatch(location -> location.getId().equals(dto.getLocationId()));

        if (!locationExists) {
            throw new RestaurantNotFoundException("Location with id " + dto.getLocationId() + " not found in restaurant " + dto.getRestaurantId());
        }

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation with id " + id + " not found"));

        reservation.setRestaurantId(dto.getRestaurantId());
        reservation.setLocationId(dto.getLocationId());
        reservation.setDate(dto.getDate());
        reservation.setTime(dto.getTime());
        reservation.setNumberOfGuests(dto.getNumberOfGuests());
        reservation.setSpecialRequests(dto.getSpecialRequests());
        reservation.setUpdatedAt(LocalDateTime.now());

        return reservationRepository.save(reservation);
    }

    @Override
    public Optional<Reservation> findById(String id) {
        return reservationRepository.findById(id);
    }

    @Override
    public List<Reservation> all() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> findByUserId(String userId) {
        return reservationRepository.findByUserId(userId);
    }

    @Override
    public List<Reservation> findByRestaurantId(String restaurantId) {
        return reservationRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public List<Reservation> findByRestaurantIdAndDate(String restaurantId, LocalDate date) {
        return reservationRepository.findByLocationIdAndDate(restaurantId, date);
    }

    @Override
    public List<Reservation> findUpcomingReservationsByUserId(String userId) {
        return reservationRepository.findByUserIdAndDateGreaterThanEqual(userId, LocalDate.now());
    }

    @Override
    public void deleteById(String id) {
        if (!reservationRepository.existsById(id)) {
            throw new ReservationNotFoundException("Reservation with id " + id + " not found");
        }
        reservationRepository.deleteById(id);
    }

    @Override
    public List<Reservation> findByLocationId(String locationId) {
        return reservationRepository.findByLocationId(locationId);
    }
}
