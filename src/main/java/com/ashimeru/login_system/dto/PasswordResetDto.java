package com.ashimeru.login_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PasswordResetDto {
  @NotBlank
  private String newPassword;
  @NotBlank
  private String confirmPassword;
  private String email;
  private String otp;
}
