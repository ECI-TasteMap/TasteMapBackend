package com.eci.edu.ieti.tastemap.reservations.exception;

/**
 * Exception thrown when a reservation is not found.
 */
public class ReservationNotFoundException extends RuntimeException {
    
    public ReservationNotFoundException(String message) {
        super(message);
    }
    
    public ReservationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
