package com.ashimeru.login_system.service;

import jakarta.mail.MessagingException;

public interface EmailService {
  boolean sendVerificationEmail(String email, String url) throws MessagingException;
  boolean sendPasswordResetEmail(String email, String otp ) throws MessagingException;
}
