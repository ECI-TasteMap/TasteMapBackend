package com.eci.edu.ieti.tastemap.user.dto;

import com.eci.edu.ieti.tastemap.user.model.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRequestDtoTest {

    @Test
    void testUserRequestDto() {
        UserRequestDto dto = new UserRequestDto();
        dto.setFullname("Test");
        dto.setPassword("password");
        dto.setEmail("test@test.com");
        dto.setRole(Role.USER);

        assertEquals("Test", dto.getFullname());
        assertEquals("password", dto.getPassword());
        assertEquals("test@test.com", dto.getEmail());
        assertEquals(Role.USER, dto.getRole());
    }
}

