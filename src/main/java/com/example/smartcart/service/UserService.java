package com.example.smartcart.service;

import com.example.smartcart.dto.UserDto;
import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    void deleteUser(Long userId);
}