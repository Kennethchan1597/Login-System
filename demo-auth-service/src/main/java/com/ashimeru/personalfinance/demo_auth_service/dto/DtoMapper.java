package com.ashimeru.personalfinance.demo_auth_service.dto;

import org.springframework.stereotype.Component;
import com.ashimeru.personalfinance.demo_auth_service.entity.UserEntity;

@Component
public class DtoMapper {

  public UserDto map(UserEntity u){
    return UserDto.builder()
    .id(u.getId())
    .userName(u.getUserName())
    .email(u.getEmail())
    .role(u.getRole())
    .build();
  }

}
