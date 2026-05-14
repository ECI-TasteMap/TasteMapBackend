package com.eci.edu.ieti.tastemap.user.controller;

import com.eci.edu.ieti.tastemap.user.dto.ChatRequestDto;
import com.eci.edu.ieti.tastemap.user.dto.ChatResponseDto;
import com.eci.edu.ieti.tastemap.user.dto.UserRequestDto;
import com.eci.edu.ieti.tastemap.user.dto.UserResponseDto;
import com.eci.edu.ieti.tastemap.user.exception.UserNotFoundException;
import com.eci.edu.ieti.tastemap.user.mapper.UserMapper;
import com.eci.edu.ieti.tastemap.user.model.User;
import com.eci.edu.ieti.tastemap.user.service.ChatService;
import com.eci.edu.ieti.tastemap.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ChatService chatService;

    public UserController(UserService userService, UserMapper userMapper, ChatService chatService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto userRequestDto) {
        User user = userService.create(userRequestDto);
        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);
        return ResponseEntity.created(URI.create("/api/v1/users/" + user.getId())).body(userResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable String id) {
        User user = userService.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> all() {
        List<User> users = userService.all();
        List<UserResponseDto> userResponseDtos = users.stream()
                .map(userMapper::toUserResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable String id, @RequestBody UserRequestDto userRequestDto) {
        User user = userService.update(id, userRequestDto);
        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);
        return ResponseEntity.ok(userResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/chat")
    public ResponseEntity<ChatResponseDto> sendMessage(@PathVariable String id, @RequestBody ChatRequestDto request) {
        ChatResponseDto response = chatService.sendMessage(id, request);
        return ResponseEntity.ok(response);
    }
}

