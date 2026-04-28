package com.eci.edu.ieti.tastemap.user.service.serviceImpl;

import com.eci.edu.ieti.tastemap.user.dto.UserRequestDto;
import com.eci.edu.ieti.tastemap.user.exception.UserNotFoundException;
import com.eci.edu.ieti.tastemap.user.mapper.UserMapper;
import com.eci.edu.ieti.tastemap.user.model.User;
import com.eci.edu.ieti.tastemap.user.repository.UserRepository;
import com.eci.edu.ieti.tastemap.user.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the UserService interface.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(UserRequestDto userRequestDto) {
        User user = userMapper.toUser(userRequestDto);
        user.setPasswordHash(passwordEncoder.encode(userRequestDto.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> all() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User update(String id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        user.setFullname(userRequestDto.getFullname());
        user.setEmail(userRequestDto.getEmail());
        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(userRequestDto.getPassword()));
        }
        user.setRole(userRequestDto.getRole());
        return userRepository.save(user);
    }
}

