package com.eci.edu.ieti.tastemap.restaurant.dto;

import com.eci.edu.ieti.tastemap.restaurant.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationOpenStatusResponseDto {
    private Location location;
    private String openStatus;
}