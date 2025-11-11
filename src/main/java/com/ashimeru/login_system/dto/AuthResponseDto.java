package com.ashimeru.login_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class AuthResponseDto {
  private UserDto user;
  private String token;
}