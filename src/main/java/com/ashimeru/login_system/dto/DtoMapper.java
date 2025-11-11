package com.ashimeru.login_system.dto;

import org.springframework.stereotype.Component;
import com.ashimeru.login_system.entity.UserEntity;

@Component
public class DtoMapper {

  public UserDto map(UserEntity u){
    return UserDto.builder()
    .id(u.getId())
    .username(u.getUserName())
    .email(u.getEmail())
    .role(u.getRole())
    .build();
  }

}
