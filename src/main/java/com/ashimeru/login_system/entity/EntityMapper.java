package com.ashimeru.login_system.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.ashimeru.login_system.dto.SignUpDto;

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
