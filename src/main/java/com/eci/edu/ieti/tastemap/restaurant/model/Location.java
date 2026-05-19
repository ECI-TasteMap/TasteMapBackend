package com.eci.edu.ieti.tastemap.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private String id;
    private String address;
    private String phone;
    private Double averageRating;
    private List<Schedule> schedules;
    private boolean reservationsEnabled = true;

    /**
     * Backwards-compatible constructor (preserves previous 5-arg signature).
     */
    public Location(String id, String address, String phone, Double averageRating, List<Schedule> schedules) {
        this.id = id;
        this.address = address;
        this.phone = phone;
        this.averageRating = averageRating;
        this.schedules = schedules;
        this.reservationsEnabled = true;
    }
}