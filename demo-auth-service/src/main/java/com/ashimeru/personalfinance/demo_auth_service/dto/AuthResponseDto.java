package com.ashimeru.personalfinance.demo_auth_service.dto;

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
