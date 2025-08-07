package com.ashimeru.personalfinance.demo_auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ashimeru.personalfinance.demo_auth_service.dto.AuthResponseDto;
import com.ashimeru.personalfinance.demo_auth_service.dto.LoginDto;
import com.ashimeru.personalfinance.demo_auth_service.dto.SignUpDto;
import jakarta.validation.Valid;

@RequestMapping(value = "/auth")
public interface AuthOperation {
  
  @PostMapping(value = "/register")
  ResponseEntity<String> register(@RequestBody @Valid SignUpDto signUpDto);

  @PostMapping(value = "/login")
  ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto login);

  @GetMapping(value = "/verify")
  ResponseEntity<String> verifyUser(@RequestParam String token);

}
