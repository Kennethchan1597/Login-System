package com.ashimeru.login_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplePayload {
  private String user;
  private String identityToken;
  private String authorizationCode;
  private String email;
  private FullName fullName;

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  @Setter
  public static class FullName {
    private String givenName;
    private String familyName;
  }
}



