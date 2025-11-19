package com.ashimeru.login_system.controller.impl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.ashimeru.login_system.controller.AuthOperation;
import com.ashimeru.login_system.dto.ApplePayload;
import com.ashimeru.login_system.dto.AuthResponseDto;
import com.ashimeru.login_system.dto.ErrorDto;
import com.ashimeru.login_system.dto.GooglePayload;
import com.ashimeru.login_system.dto.LoginDto;
import com.ashimeru.login_system.dto.PasswordForgotDto;
import com.ashimeru.login_system.dto.PasswordResetDto;
import com.ashimeru.login_system.dto.SignUpDto;
import com.ashimeru.login_system.dto.UserDto;
import com.ashimeru.login_system.dto.VerifyOtpDto;
import com.ashimeru.login_system.entity.PasswordForgotOtpEntity;
import com.ashimeru.login_system.entity.UserEntity;
import com.ashimeru.login_system.entity.VerificationTokenEntity;
import com.ashimeru.login_system.exception.AppException;
import com.ashimeru.login_system.security.JwtUtil;
import com.ashimeru.login_system.service.AuthService;
import com.ashimeru.login_system.service.EmailService;
import com.ashimeru.login_system.service.PasswordResetService;
import com.ashimeru.login_system.service.VerificationTokenService;
import jakarta.mail.MessagingException;

@RestController
public class AuthController implements AuthOperation {
  @Autowired
  private AuthService authService;
  @Autowired
  private VerificationTokenService verificationTokenService;
  @Autowired
  private PasswordResetService passwordResetService;
  @Autowired
  private EmailService emailService;
  @Autowired
  private JwtUtil jwtUtil;

  @Override
  public ResponseEntity<String> register(SignUpDto signUpDto) {
    this.authService.register(signUpDto);
    return ResponseEntity.ok().body(
        "Registration Email has been sent, link will be expired in 30 minutes");
  }

  @Override
  public ResponseEntity<AuthResponseDto> login(LoginDto login) {
    UserDto userDto = this.authService.login(login);
    String token = this.jwtUtil.generateToken(userDto);
    return ResponseEntity.ok().body(new AuthResponseDto(userDto, token));
  }

  @Override
  public ResponseEntity<AuthResponseDto> loginWithApple(ApplePayload payload)
      throws Exception {
    try {
      UserDto userDto = authService.loginWithApple(payload);
      String token = jwtUtil.generateToken(userDto);
      return ResponseEntity.ok(new AuthResponseDto(userDto, token));
    } catch (Exception e) {
      throw new AppException(ErrorDto.Code.LOGIN_ERROR);
    }
  }

  @Override
  public ResponseEntity<AuthResponseDto> loginWithGoogle(GooglePayload payload) {
    try {
      UserDto userDto = authService.loginWithGoogle(payload);
      String token = jwtUtil.generateToken(userDto);
      return ResponseEntity.ok(new AuthResponseDto(userDto, token));
    } catch (Exception e) {
      throw new AppException(ErrorDto.Code.LOGIN_ERROR);
    }
  }

  @Override
  public ResponseEntity<String> verifyUser(String token) {
    UserEntity user = this.verificationTokenService.VerifyUser(token);
    this.authService.saveUser(user);
    VerificationTokenEntity usedToken =
        this.verificationTokenService.findByToken(token).get();
    this.verificationTokenService.deleteToken(usedToken);
    return ResponseEntity.ok().body("Registration is successful");
  }

  @Override
  public ResponseEntity<String> forgotPassword(PasswordForgotDto dto) {
    UserEntity userEntity = this.authService.findByEmail(dto.getEmail())
        .orElseThrow(() -> new AppException(ErrorDto.Code.USER_NOT_FOUND));
    Optional<PasswordForgotOtpEntity> oldOtp =
        this.passwordResetService.findByUser(userEntity);
    if (oldOtp.isPresent()) {
      PasswordForgotOtpEntity PasswordForgotOtpEntity = oldOtp.get();
      if (!PasswordForgotOtpEntity.isExpired()) {
        return ResponseEntity.ok().body("Email Has Been Sent.");
      }
      this.passwordResetService.deleteOtp(PasswordForgotOtpEntity);
    }
    String newOtp = this.passwordResetService.generateForgotPasswordOtp();
    try {
      this.emailService.sendPasswordResetEmail(dto.getEmail(), newOtp);
      this.passwordResetService.savePasswordForgotOtp(newOtp, userEntity);
      return ResponseEntity.ok()
          .body("Email sent, please reset your password in 15 minutes.");
    } catch (MessagingException e) {
      throw new AppException(ErrorDto.Code.EMAIL_SEND_FAILED);
    }
  }

  @Override
  public ResponseEntity<String> resetPassword(PasswordResetDto dto) {
    String otp = dto.getOtp();
    String userEmail = dto.getEmail();
    UserEntity userEntity = this.authService.findByEmail(userEmail)
        .orElseThrow(() -> new AppException(ErrorDto.Code.USER_NOT_FOUND));
    PasswordForgotOtpEntity otpEntity =
        this.passwordResetService.findByUserAndOtp(userEntity, otp)
            .orElseThrow(() -> new AppException(ErrorDto.Code.TOKEN_INVALID));
    this.passwordResetService.resetPassword(userEntity, dto);
    this.authService.saveUser(userEntity);
    this.passwordResetService.deleteOtp(otpEntity);
    return ResponseEntity.ok().body("Password has been reset.");
  }

  @Override
  public ResponseEntity<String> verifyOtp(VerifyOtpDto dto) {
    String userEmail = dto.getEmail();
    UserEntity userEntity = this.authService.findByEmail(userEmail)
        .orElseThrow(() -> new AppException(ErrorDto.Code.USER_NOT_FOUND));
    boolean result =
        this.passwordResetService.verifyOtp(dto.getInputOtp(), userEntity);
    return result ? ResponseEntity.ok().body("Verified")
        : ResponseEntity.badRequest().body("Verification failed");
  }
}
