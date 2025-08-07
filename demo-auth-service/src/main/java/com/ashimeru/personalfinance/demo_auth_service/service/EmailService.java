package com.ashimeru.personalfinance.demo_auth_service.service;

import jakarta.mail.MessagingException;

public interface EmailService {
  boolean sendVerificationEmail(String email, String url) throws MessagingException;
}
