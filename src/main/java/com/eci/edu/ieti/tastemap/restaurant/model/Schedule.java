package com.eci.edu.ieti.tastemap.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    private Set<DayOfWeek> days;
    private String openTime;
    private String closeTime;
    private boolean closed;
}