package com.example.smartcart.service.impl;

import com.example.smartcart.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("SmartCart - Password Reset Request");

        // In a real frontend application, this would be a URL to your reset password page
        String resetUrl = "http://localhost:8080/reset-password?token=" + token;

        message.setText("To reset your password, click the link below:\n" + resetUrl
                + "\n\nIf you did not request a password reset, please ignore this email.");

        mailSender.send(message);
    }
}