package com.ashimeru.login_system.exception;

import com.ashimeru.login_system.dto.ErrorDto;

public class AppException extends RuntimeException {
  private final ErrorDto.Code code;

  public AppException(ErrorDto.Code code) {
    super(code.getMessage());
    this.code = code;
  }

  public ErrorDto.Code getCode() {
    return code;
  }
}
