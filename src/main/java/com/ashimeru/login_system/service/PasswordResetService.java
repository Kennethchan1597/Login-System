package com.ashimeru.login_system.service;

import java.util.Optional;
import com.ashimeru.login_system.dto.PasswordResetDto;
import com.ashimeru.login_system.entity.PasswordForgotOtpEntity;
import com.ashimeru.login_system.entity.UserEntity;

public interface PasswordResetService {
  void savePasswordForgotOtp(String Otp, UserEntity userEntity);

  void deleteOtp(PasswordForgotOtpEntity Otp);

  Optional<PasswordForgotOtpEntity> findByOtp(String Otp);

  Optional<PasswordForgotOtpEntity> findByUser(UserEntity userEntity);

  Optional<PasswordForgotOtpEntity> findByUserAndOtp(UserEntity userEntity, String otp);

  String generateForgotPasswordOtp();
 
  void resetPassword(UserEntity userEntity, PasswordResetDto dto);

  boolean verifyOtp(String inputOtp, UserEntity userEntity);

}
