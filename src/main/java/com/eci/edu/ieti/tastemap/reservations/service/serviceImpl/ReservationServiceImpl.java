package com.eci.edu.ieti.tastemap.reservations.service.serviceImpl;

import com.eci.edu.ieti.tastemap.reservations.dto.ReservationRequestDto;
import com.eci.edu.ieti.tastemap.reservations.exception.ReservationNotFoundException;
import com.eci.edu.ieti.tastemap.reservations.mapper.ReservationMapper;
import com.eci.edu.ieti.tastemap.reservations.model.Reservation;
import com.eci.edu.ieti.tastemap.reservations.repository.ReservationRepository;
import com.eci.edu.ieti.tastemap.reservations.service.ReservationService;
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
    
    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }
    
    @Override
    public Reservation create(ReservationRequestDto reservationRequestDto) {
        Reservation reservation = reservationMapper.toReservation(reservationRequestDto);
        reservation.setCreatedAt(LocalDateTime.now());
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
        return reservationRepository.findByRestaurantIdAndDate(restaurantId, date);
    }
    
    @Override
    public List<Reservation> findUpcomingReservationsByUserId(String userId) {
        return reservationRepository.findByUserIdAndDateGreaterThanEqual(userId, LocalDate.now());
    }
    
    @Override
    public Reservation update(String id, ReservationRequestDto reservationRequestDto) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation with id " + id + " not found"));
        
        reservation.setUserId(reservationRequestDto.getUserId());
        reservation.setRestaurantId(reservationRequestDto.getRestaurantId());
        reservation.setDate(reservationRequestDto.getDate());
        reservation.setTime(reservationRequestDto.getTime());
        reservation.setNumberOfGuests(reservationRequestDto.getNumberOfGuests());
        reservation.setSpecialRequests(reservationRequestDto.getSpecialRequests());
        reservation.setUpdatedAt(LocalDateTime.now());
        
        return reservationRepository.save(reservation);
    }
    
    @Override
    public void deleteById(String id) {
        if (!reservationRepository.existsById(id)) {
            throw new ReservationNotFoundException("Reservation with id " + id + " not found");
        }
        reservationRepository.deleteById(id);
    }
}
