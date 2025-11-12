package com.ashimeru.login_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ashimeru.login_system.dto.AuthResponseDto;
import com.ashimeru.login_system.dto.LoginDto;
import com.ashimeru.login_system.dto.PasswordForgotDto;
import com.ashimeru.login_system.dto.PasswordResetDto;
import com.ashimeru.login_system.dto.SignUpDto;
import com.ashimeru.login_system.dto.VerifyOtpDto;
import jakarta.validation.Valid;

@RequestMapping(value = "/api/auth")
public interface AuthOperation {
  
  @PostMapping(value = "/register")
  ResponseEntity<String> register(@RequestBody @Valid SignUpDto signUpDto);

  @PostMapping(value = "/login")
  ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto login);

  @GetMapping(value = "/verify")
  ResponseEntity<String> verifyUser(@RequestParam String token);

  @PostMapping(value = "/password/forgot")
  ResponseEntity<String> forgotPassword(@RequestBody PasswordForgotDto dto);

  @PostMapping(value = "/password/reset")
  ResponseEntity<String> resetPassword(@RequestBody PasswordResetDto dto);

  @PostMapping(value = "/password/forgot/otp")
  ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpDto dto);

}
