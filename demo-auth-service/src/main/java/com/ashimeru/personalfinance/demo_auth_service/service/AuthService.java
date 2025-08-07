package com.ashimeru.personalfinance.demo_auth_service.service;

import java.util.Optional;
import com.ashimeru.personalfinance.demo_auth_service.dto.LoginDto;
import com.ashimeru.personalfinance.demo_auth_service.dto.SignUpDto;
import com.ashimeru.personalfinance.demo_auth_service.dto.UserDto;
import com.ashimeru.personalfinance.demo_auth_service.entity.UserEntity;

public interface AuthService {
  Optional<UserEntity> findByUserName(String name);

  Optional<UserEntity> findByEmail(String email);

  UserEntity saveUser(UserEntity user);

  void register(SignUpDto signUpDto);

  UserDto login(LoginDto loginDto);
}
