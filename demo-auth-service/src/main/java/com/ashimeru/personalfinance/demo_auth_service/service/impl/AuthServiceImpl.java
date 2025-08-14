package com.ashimeru.personalfinance.demo_auth_service.service.impl;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.ashimeru.personalfinance.demo_auth_service.dto.DtoMapper;
import com.ashimeru.personalfinance.demo_auth_service.dto.ErrorDto;
import com.ashimeru.personalfinance.demo_auth_service.dto.LoginDto;
import com.ashimeru.personalfinance.demo_auth_service.dto.SignUpDto;
import com.ashimeru.personalfinance.demo_auth_service.dto.UserDto;
import com.ashimeru.personalfinance.demo_auth_service.entity.EntityMapper;
import com.ashimeru.personalfinance.demo_auth_service.entity.UserEntity;
import com.ashimeru.personalfinance.demo_auth_service.exception.AppException;
import com.ashimeru.personalfinance.demo_auth_service.repository.AuthRepository;
import com.ashimeru.personalfinance.demo_auth_service.security.CustomUserDetails;
import com.ashimeru.personalfinance.demo_auth_service.service.AuthService;
import com.ashimeru.personalfinance.demo_auth_service.service.EmailService;
import com.ashimeru.personalfinance.demo_auth_service.service.VerificationTokenService;
import jakarta.mail.MessagingException;

@Service
public class AuthServiceImpl implements AuthService {
  @Autowired
  private AuthRepository authRepository;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private EntityMapper entityMapper;
  @Autowired
  private DtoMapper dtoMapper;
  @Autowired
  private VerificationTokenService verificationTokenService;
  @Autowired
  private EmailService emailService;
  

  @Override
  public Optional<UserEntity> findByUserName(String name) {
    return this.authRepository.findByUserName(name);
  }

  @Override
  public Optional<UserEntity> findByEmail(String email) {
    return this.authRepository.findByEmail(email);
  }

  @Override
  public UserEntity saveUser(UserEntity user) {
    return this.authRepository.save(user);
  }

  @Override
  public void register(SignUpDto signUpDto) {
    if (this.findByEmail(signUpDto.getEmail()).isPresent())
      throw new AppException(ErrorDto.Code.EMAIL_EXISTED);
    if (this.findByUserName(signUpDto.getUserName()).isPresent())
      throw new AppException(ErrorDto.Code.USER_EXISTED);
    UserEntity entity = this.entityMapper.map(signUpDto);
    String token = UUID.randomUUID().toString();
    String url = "http://localhost:8090/auth/verify?token=" + token;
    try {
      this.emailService.sendVerificationEmail(entity.getEmail(), url);
      this.saveUser(entity);
      this.verificationTokenService.saveTokenForUser(token, entity);
    } catch (MessagingException e) {
      throw new AppException(ErrorDto.Code.EMAIL_SEND_FAILED);
    }
  }

  @Override
  public UserDto login(LoginDto loginDto) {
    this.findByUserName(loginDto.getUsername()).orElseThrow(() -> new AppException(ErrorDto.Code.USER_NOT_FOUND));
    if (loginDto.getPassword() == null || loginDto.getPassword().isEmpty())
      throw new AppException(ErrorDto.Code.INVALID_PASSWORD);
    try {
      Authentication auth = this.authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
              loginDto.getPassword()));
      CustomUserDetails userd = (CustomUserDetails) auth.getPrincipal();
      UserEntity user = userd.getUserEntity();
      return this.dtoMapper.map(user);
    } catch (BadCredentialsException e) {
      throw new AppException(ErrorDto.Code.INVALID_PASSWORD);
    }
  }

}
