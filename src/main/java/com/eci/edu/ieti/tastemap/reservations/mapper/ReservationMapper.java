package com.eci.edu.ieti.tastemap.reservations.mapper;

import com.eci.edu.ieti.tastemap.reservations.dto.ReservationRequestDto;
import com.eci.edu.ieti.tastemap.reservations.dto.ReservationResponseDto;
import com.eci.edu.ieti.tastemap.reservations.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for converting between Reservation, ReservationRequestDto, and ReservationResponseDto.
 */
@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Reservation toReservation(ReservationRequestDto reservationRequestDto);

    ReservationResponseDto toReservationResponseDto(Reservation reservation);
}
