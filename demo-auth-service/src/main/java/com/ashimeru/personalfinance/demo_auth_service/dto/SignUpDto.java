package com.ashimeru.personalfinance.demo_auth_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SignUpDto {
  @NotBlank
  @JsonProperty(value = "username")
  private String userName;
  @Size(min = 8, message = "Password must be longer than 8 characters")
  private String password;
  @Email
  @NotBlank
  private String email;
}
