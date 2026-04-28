package com.eci.edu.ieti.tastemap.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void testUser() {
        User user = new User();
        user.setId("1");
        user.setFullname("Test");
        user.setPasswordHash("hash");
        user.setEmail("test@test.com");
        user.setRole(Role.USER);

        assertEquals("1", user.getId());
        assertEquals("Test", user.getFullname());
        assertEquals("hash", user.getPasswordHash());
        assertEquals("test@test.com", user.getEmail());
        assertEquals(Role.USER, user.getRole());
    }
}

