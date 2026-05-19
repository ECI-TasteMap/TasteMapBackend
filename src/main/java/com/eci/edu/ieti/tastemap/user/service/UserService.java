package com.eci.edu.ieti.tastemap.user.service;

import com.eci.edu.ieti.tastemap.user.dto.UserRequestDto;
import com.eci.edu.ieti.tastemap.user.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for user-related operations.
 */
public interface UserService {
    User create(UserRequestDto userRequestDto);
    Optional<User> findById(String id);
    Optional<User> findBySupabaseId(String supabaseId);
    List<User> all();
    void deleteById(String id);
    User update(String id, UserRequestDto userRequestDto);
}

