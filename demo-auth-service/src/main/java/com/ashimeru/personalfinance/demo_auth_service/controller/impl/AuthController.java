package com.ashimeru.personalfinance.demo_auth_service.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.ashimeru.personalfinance.demo_auth_service.controller.AuthOperation;
import com.ashimeru.personalfinance.demo_auth_service.dto.AuthResponseDto;
import com.ashimeru.personalfinance.demo_auth_service.dto.LoginDto;
import com.ashimeru.personalfinance.demo_auth_service.dto.SignUpDto;
import com.ashimeru.personalfinance.demo_auth_service.dto.UserDto;
import com.ashimeru.personalfinance.demo_auth_service.entity.UserEntity;
import com.ashimeru.personalfinance.demo_auth_service.entity.VerificationTokenEntity;
import com.ashimeru.personalfinance.demo_auth_service.security.JwtUtil;
import com.ashimeru.personalfinance.demo_auth_service.service.AuthService;
import com.ashimeru.personalfinance.demo_auth_service.service.VerificationTokenService;

@RestController
public class AuthController implements AuthOperation {
  @Autowired
  private AuthService authService;
  @Autowired
  private VerificationTokenService verificationTokenService;
  @Autowired
  private JwtUtil jwtUtil;
  

  @Override
  public ResponseEntity<String> register(SignUpDto signUpDto) {
    this.authService.register(signUpDto);
    return ResponseEntity.ok().body("Registration Email has been sent, link will be expired in 30 minutes");
  }

  @Override
  public ResponseEntity<AuthResponseDto> login(LoginDto login) {
    UserDto userDto = this.authService.login(login);
    String token = this.jwtUtil.generateToken(userDto);
    return ResponseEntity.ok().body(new AuthResponseDto(userDto, token));
  }

  @Override
  public ResponseEntity<String> verifyUser(String token) {
    UserEntity user = this.verificationTokenService.VerifyUser(token);
    this.authService.saveUser(user);
    VerificationTokenEntity usedToken = this.verificationTokenService.findByToken(token).get();
    this.verificationTokenService.deleteToken(usedToken);
    return ResponseEntity.ok().body("Registration is successful");
  }

}
