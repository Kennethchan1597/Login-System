package com.ashimeru.login_system.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ashimeru.login_system.dto.ErrorDto;
import com.ashimeru.login_system.dto.PasswordResetDto;
import com.ashimeru.login_system.entity.PasswordForgotOtpEntity;
import com.ashimeru.login_system.entity.UserEntity;
import com.ashimeru.login_system.exception.AppException;
import com.ashimeru.login_system.repository.PasswordForgotOtpRepository;
import com.ashimeru.login_system.service.PasswordResetService;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {
  @Autowired
  private PasswordForgotOtpRepository passwordForgotOtpRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public void savePasswordForgotOtp(String otp, UserEntity userEntity) {
    this.passwordForgotOtpRepository.deleteByUser(userEntity);
    PasswordForgotOtpEntity entity = PasswordForgotOtpEntity.builder()
        .expDateTime(LocalDateTime.now().plusMinutes(15)).otp(otp)
        .user(userEntity).build();
    this.passwordForgotOtpRepository.save(entity);
  }

  @Override
  public void deleteOtp(PasswordForgotOtpEntity PasswordForgotOtp) {
    this.passwordForgotOtpRepository.delete(PasswordForgotOtp);
  }

  @Override
  public Optional<PasswordForgotOtpEntity> findByOtp(String otp) {
    return this.passwordForgotOtpRepository.findByOtp(otp);
  }

  @Override
  public Optional<PasswordForgotOtpEntity> findByUser(UserEntity userEntity) {
    return this.passwordForgotOtpRepository.findByUser(userEntity);
  }

  @Override
  public String generateForgotPasswordOtp() {
    Integer otp = (int) (Math.random() * 900000) + 100000; // ensures 100000-999999
    return otp.toString();
  }

  @Override
  public void resetPassword(UserEntity user, PasswordResetDto dto) {
    user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
  }

  @Override
  public Optional<PasswordForgotOtpEntity> findByUserAndOtp(
      UserEntity userEntity, String otp) {
    return this.passwordForgotOtpRepository.findByUserAndOtp(userEntity, otp);
  }

  @Override
  public boolean verifyOtp(String inputOtp, UserEntity userEntity) {
    PasswordForgotOtpEntity otpEntity =
        this.findByUserAndOtp(userEntity, inputOtp)
            .orElseThrow(() -> new AppException(ErrorDto.Code.TOKEN_INVALID));
    if (otpEntity.isExpired()) {
      this.deleteOtp(otpEntity);
      throw new AppException(ErrorDto.Code.TOKEN_EXPIRED);
    }
    this.passwordForgotOtpRepository.save(otpEntity);
    return true;
  }
}

