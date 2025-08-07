package com.ashimeru.personalfinance.demo_auth_service.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.ashimeru.personalfinance.demo_auth_service.dto.SignUpDto;

@Component
public class EntityMapper {
  @Autowired
  private PasswordEncoder passwordEncoder;

  public UserEntity map(SignUpDto sd) {
    return UserEntity.builder()
    .email(sd.getEmail())
    .userName(sd.getUserName())
    .password(this.passwordEncoder.encode(sd.getPassword()))
    .role(UserRole.USER)
    .build();
  }
}
