package com.eci.edu.ieti.tastemap.reservations.exception;

import com.eci.edu.ieti.tastemap.restaurant.exception.RestaurantNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ReservationExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ReservationExceptionHandler.class);

    @ExceptionHandler({RestaurantNotFoundException.class, ReservationNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, exception, request);
    }

    @ExceptionHandler(InvalidReservationException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidReservation(InvalidReservationException exception,
                                                                        HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception, request);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status,
                                                              RuntimeException exception,
                                                              HttpServletRequest request) {
        log.warn("Reservation request failed: {} {} -> {}", request.getMethod(), request.getRequestURI(), exception.getMessage(), exception);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("path", request.getRequestURI());
        body.put("message", exception.getMessage());

        return ResponseEntity.status(status).body(body);
    }
}