package com.eci.edu.ieti.tastemap.user.controller;

import com.eci.edu.ieti.tastemap.user.dto.UserRequestDto;
import com.eci.edu.ieti.tastemap.user.dto.UserResponseDto;
import com.eci.edu.ieti.tastemap.user.mapper.UserMapper;
import com.eci.edu.ieti.tastemap.user.model.Role;
import com.eci.edu.ieti.tastemap.user.model.User;
import com.eci.edu.ieti.tastemap.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private User user;
    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        user = new User("1", "Test User", "password", "test@test.com", Role.USER);
        userRequestDto = new UserRequestDto("Test User", "password", "test@test.com", Role.USER);
        userResponseDto = new UserResponseDto("1", "Test User", "test@test.com", Role.USER);
    }

    @Test
    void testCreate() {
        when(userService.create(userRequestDto)).thenReturn(user);
        when(userMapper.toUserResponseDto(user)).thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = userController.create(userRequestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponseDto, response.getBody());
        verify(userService, times(1)).create(userRequestDto);
    }

    @Test
    void testFindById() {
        when(userService.findById("1")).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDto(user)).thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = userController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponseDto, response.getBody());
        verify(userService, times(1)).findById("1");
    }

    @Test
    void testAll() {
        when(userService.all()).thenReturn(Collections.singletonList(user));
        when(userMapper.toUserResponseDto(user)).thenReturn(userResponseDto);

        ResponseEntity<List<UserResponseDto>> response = userController.all();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(userResponseDto, response.getBody().get(0));
        verify(userService, times(1)).all();
    }

    @Test
    void testUpdate() {
        when(userService.update("1", userRequestDto)).thenReturn(user);
        when(userMapper.toUserResponseDto(user)).thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = userController.update("1", userRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponseDto, response.getBody());
        verify(userService, times(1)).update("1", userRequestDto);
    }

    @Test
    void testDeleteById() {
        doNothing().when(userService).deleteById("1");

        ResponseEntity<Void> response = userController.deleteById("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteById("1");
    }
}

