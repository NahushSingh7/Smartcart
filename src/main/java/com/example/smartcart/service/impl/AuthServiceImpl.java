package com.example.smartcart.service.impl;


import com.example.smartcart.dto.LoginDto;
import com.example.smartcart.dto.RegisterDto;
import com.example.smartcart.exception.ResourceNotFoundException;
import com.example.smartcart.entity.User;
import com.example.smartcart.repository.UserRepository;
import com.example.smartcart.security.JwtTokenProvider;
import com.example.smartcart.service.AuthService;
import com.example.smartcart.service.EmailService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    private final EmailService emailService; // Add this

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           EmailService emailService) { // Add EmailService
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService; // Add this
    }

//    public AuthServiceImpl(AuthenticationManager authenticationManager,
//                           UserRepository userRepository,
//                           PasswordEncoder passwordEncoder,
//                           JwtTokenProvider jwtTokenProvider) {
//        this.authenticationManager = authenticationManager;
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtTokenProvider = jwtTokenProvider;
//    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String register(RegisterDto registerDto) {
        if (userRepository.findByUsername(registerDto.getUsername()).isPresent()) {
            throw new ResourceNotFoundException("User", "username", 0); // Simplified exception
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(registerDto.getRole());

        userRepository.save(user);

        return "User registered successfully!";
    }
    
    @Override
    public void generatePasswordResetToken(String email) {
        User user = userRepository.findByEmail(email) // You may need to add findByEmail to UserRepository
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", 0));

        String token = UUID.randomUUID().toString();
        user.setPasswordResetToken(token);
        user.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(1)); // Token is valid for 1 hour

        userRepository.save(user);
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token) // You need to add this method to UserRepository
                .orElseThrow(() -> new RuntimeException("Invalid password reset token."));

        if (user.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Password reset token has expired.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null); // Clear the token
        user.setPasswordResetTokenExpiry(null);

        userRepository.save(user);
    }
}