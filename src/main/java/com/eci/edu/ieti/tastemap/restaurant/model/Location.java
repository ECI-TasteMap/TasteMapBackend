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
}