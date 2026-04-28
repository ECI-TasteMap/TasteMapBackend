package com.eci.edu.ieti.tastemap.user.dto;

import com.eci.edu.ieti.tastemap.user.model.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserResponseDtoTest {

    @Test
    void testUserResponseDto() {
        UserResponseDto dto = new UserResponseDto();
        dto.setId("1");
        dto.setFullname("Test");
        dto.setEmail("test@test.com");
        dto.setRole(Role.USER);

        assertEquals("1", dto.getId());
        assertEquals("Test", dto.getFullname());
        assertEquals("test@test.com", dto.getEmail());
        assertEquals(Role.USER, dto.getRole());
    }
}

