package com.ashimeru.personalfinance.demo_auth_service.dto;

import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ErrorDto {
  private String message;
  private Code code;
  private int status;
  private ZonedDateTime timestamp;

  public static enum Code{
    USER_NOT_FOUND(1, "User not found", HttpStatus.NOT_FOUND),
    USER_EXISTED(2, "Username is already existed", HttpStatus.CONFLICT),
    INVALID_PASSWORD(3, "Invalid Password", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(4, "Expired token", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(5, "Invalid token", HttpStatus.UNAUTHORIZED),
    EMAIL_SEND_FAILED(6, "Failed to send verification email", HttpStatus.BAD_REQUEST),
    USER_IS_VERIFIED(7, "User is already verified", HttpStatus.CONFLICT),
    EMAIL_EXISTED(8, "Email is already registered", HttpStatus.CONFLICT);

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    private Code(int code, String message, HttpStatus httpStatus) {
      this.code = code;
      this.message = message;
      this.httpStatus = httpStatus;
    }

    public int getCode() {
      return this.code;
    }

    public String getMessage() {
      return this.message;
    }

    public HttpStatus getHttpStatus() {
      return this.httpStatus;
    }

    public static Code getFromCode(int code) {
      for (Code c : Code.values()) {
        if(c.getCode() == code)
        return c;
      }
      throw new IllegalArgumentException("No enum constant with code " + code);
    }
  }
}
