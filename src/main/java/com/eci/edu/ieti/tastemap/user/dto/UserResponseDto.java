package com.eci.edu.ieti.tastemap.user.dto;

import com.eci.edu.ieti.tastemap.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user data in responses.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String id;
    private String fullname;
    private String email;
    private Role role;
}

