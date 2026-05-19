package com.eci.edu.ieti.tastemap.user.service;

import com.eci.edu.ieti.tastemap.user.dto.UserRequestDto;
import com.eci.edu.ieti.tastemap.user.exception.UserNotFoundException;
import com.eci.edu.ieti.tastemap.user.mapper.UserMapper;
import com.eci.edu.ieti.tastemap.user.model.Role;
import com.eci.edu.ieti.tastemap.user.model.User;
import com.eci.edu.ieti.tastemap.user.repository.UserRepository;
import com.eci.edu.ieti.tastemap.user.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequestDto userRequestDto;

    @BeforeEach
    void setUp() {
        user = new User("1", "supabase-123", "Test User", "encodedPassword", "test@test.com", Role.USER);
        userRequestDto = new UserRequestDto("supabase-123", "Test User", "password", "test@test.com", Role.USER);
    }

    @Test
    void testCreate() {
        when(userMapper.toUser(userRequestDto)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.create(userRequestDto);

        assertEquals(user, createdUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindById() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findById("1");

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(userRepository, times(1)).findById("1");
    }

    @Test
    void testAll() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<User> users = userService.all();

        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testDeleteById() {
        when(userRepository.existsById("1")).thenReturn(true);
        doNothing().when(userRepository).deleteById("1");

        userService.deleteById("1");

        verify(userRepository, times(1)).deleteById("1");
    }

    @Test
    void testDeleteByIdNotFound() {
        when(userRepository.existsById("1")).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteById("1"));
        verify(userRepository, never()).deleteById("1");
    }

    @Test
    void testUpdate() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = userService.update("1", userRequestDto);

        assertEquals(user, updatedUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateNotFound() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update("1", userRequestDto));
        verify(userRepository, never()).save(any(User.class));
    }
}

