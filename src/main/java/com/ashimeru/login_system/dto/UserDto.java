package com.ashimeru.login_system.dto;

import com.ashimeru.login_system.entity.UserRole;
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
  private String username;
  private String email;
  private UserRole role;
}
