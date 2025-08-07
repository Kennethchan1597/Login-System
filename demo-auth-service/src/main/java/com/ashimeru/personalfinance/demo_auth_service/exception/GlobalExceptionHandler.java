package com.ashimeru.personalfinance.demo_auth_service.exception;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.ashimeru.personalfinance.demo_auth_service.dto.ErrorDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = AppException.class)
  public ResponseEntity<ErrorDto> handleAppException(AppException ex) {
    ErrorDto errorDto = ErrorDto.builder()
        .code(ex.getCode())
        .message(ex.getMessage())
        .status(ex.getCode().getHttpStatus().value())
        .timestamp(ZonedDateTime.now())
        .build();
        return ResponseEntity.status(ex.getCode().getHttpStatus()).body(errorDto);
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->{
      errors.put(error.getField(), error.getDefaultMessage());
    });
    return ResponseEntity.badRequest().body(errors);
  }

}


