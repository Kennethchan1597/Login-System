package com.ashimeru.personalfinance.demo_auth_service.dto;

import com.ashimeru.personalfinance.demo_auth_service.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserDto {
  private Long id;
  private String userName;
  private String email;
  private UserRole role;
}
