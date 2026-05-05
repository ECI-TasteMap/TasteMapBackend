package com.eci.edu.ieti.tastemap.user.dto;

import com.eci.edu.ieti.tastemap.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user creation and update requests.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private String fullname;
    private String password;
    private String email;
    private Role role;
}
