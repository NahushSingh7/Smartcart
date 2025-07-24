package com.example.smartcart.service;


public interface EmailService {
    void sendPasswordResetEmail(String to, String token);
}