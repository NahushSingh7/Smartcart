package com.example.smartcart.controller;

import com.example.smartcart.dto.ForgotPasswordDto;
import com.example.smartcart.dto.JwtAuthResponse;
import com.example.smartcart.dto.LoginDto;
import com.example.smartcart.dto.RegisterDto;
import com.example.smartcart.dto.ResetPasswordDto;
import com.example.smartcart.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);
        return ResponseEntity.ok(new JwtAuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        authService.generatePasswordResetToken(forgotPasswordDto.getEmail());
        return ResponseEntity.ok("Password reset link has been sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, 
                                                @RequestBody ResetPasswordDto resetPasswordDto) {
        authService.resetPassword(token, resetPasswordDto.getNewPassword());
        return ResponseEntity.ok("Password has been reset successfully.");
    }
}