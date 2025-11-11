package com.ashimeru.login_system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.ashimeru.login_system.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
  @Autowired
  private JavaMailSender emailSender;

  @Override
  public boolean sendVerificationEmail(String email, String url)
      throws MessagingException {

    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    String subject = "Account Verification";
    String htmlMessage =
        """
                <html>
                  <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                    <h2>Welcome to Our App!</h2>
                    <p>Hi there,</p>
                    <p>Thanks for signing up. Please verify your email address by clicking the link below:</p>
                    <p>
                      <a href="%s" style="background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px;">
                        Verify Email
                      </a>
                    </p>
                    <p>If you didn't request this, just ignore this email.</p>
                    <p>– The Team</p>
                  </body>
                </html>
            """
            .formatted(url);

    helper.setTo(email);
    helper.setSubject(subject);
    helper.setText(htmlMessage, true);

    emailSender.send(message);
    return true;
  }

  @Override
  public boolean sendPasswordResetEmail(String email, String otp)
      throws MessagingException {
    // Generate a 6-digit OTP

    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    String subject = "Your One-Time Password (OTP)";
    String htmlMessage =
        """
            <html>
              <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                <h2>Your OTP Code</h2>
                <p>Use the following 6-digit code to reset your password. It expires in 15 minutes.</p>
                <p style="font-size: 24px; font-weight: bold; color: #B04040;">%s</p>
                <p>If you didn't request this, just ignore this email.</p>
                <p>– The Team</p>
              </body>
            </html>
            """
            .formatted(otp);

    helper.setTo(email);
    helper.setSubject(subject);
    helper.setText(htmlMessage, true);

    emailSender.send(message);

    // optionally return the OTP if you want to save it to database
    // or handle it in your service layer
    System.out.println("Generated OTP: " + otp);

    return true;
  }
}
