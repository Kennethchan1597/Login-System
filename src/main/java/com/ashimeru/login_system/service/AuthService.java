package com.ashimeru.login_system.service;

import java.util.Optional;
import com.ashimeru.login_system.dto.ApplePayload;
import com.ashimeru.login_system.dto.GooglePayload;
import com.ashimeru.login_system.dto.LoginDto;
import com.ashimeru.login_system.dto.SignUpDto;
import com.ashimeru.login_system.dto.UserDto;
import com.ashimeru.login_system.entity.UserEntity;

public interface AuthService {
  Optional<UserEntity> findByUserName(String name);

  Optional<UserEntity> findByEmail(String email);

  UserEntity saveUser(UserEntity user);

  void register(SignUpDto signUpDto);

  UserDto login(LoginDto loginDto);

  UserDto loginWithApple(ApplePayload payload) throws Exception;

  UserDto loginWithGoogle(GooglePayload payload);

}
