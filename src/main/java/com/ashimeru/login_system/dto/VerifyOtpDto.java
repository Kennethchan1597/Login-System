package com.ashimeru.login_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifyOtpDto {
  private String email;
  private String inputOtp;
}
