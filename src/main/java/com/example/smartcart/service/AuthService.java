package com.example.smartcart.service;


import com.example.smartcart.dto.LoginDto;
import com.example.smartcart.dto.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
    
    void generatePasswordResetToken(String email);
    void resetPassword(String token, String newPassword);
}