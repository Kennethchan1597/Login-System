package com.ashimeru.personalfinance.demo_auth_service.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ashimeru.personalfinance.demo_auth_service.dto.ErrorDto;
import com.ashimeru.personalfinance.demo_auth_service.entity.UserEntity;
import com.ashimeru.personalfinance.demo_auth_service.entity.VerificationTokenEntity;
import com.ashimeru.personalfinance.demo_auth_service.exception.AppException;
import com.ashimeru.personalfinance.demo_auth_service.repository.VerificationTokenRepository;
import com.ashimeru.personalfinance.demo_auth_service.service.VerificationTokenService;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {
  @Autowired
  private VerificationTokenRepository verificationTokenRepository;

  @Override
  public void saveTokenForUser(String token, UserEntity userEntity) {
    VerificationTokenEntity entity = VerificationTokenEntity.builder()
    .expDateTime(LocalDateTime.now().plusMinutes(30))
    .token(token)
    .user(userEntity)
    .build();
    this.verificationTokenRepository.save(entity);
  }

  @Override
  public VerificationTokenEntity save(VerificationTokenEntity verificationTokenEntity) {
    return this.verificationTokenRepository.save(verificationTokenEntity);
  }

  @Override
  public UserEntity VerifyUser(String token) {
    VerificationTokenEntity verificationToken = this.findByToken(token)
    .orElseThrow( () -> new AppException(ErrorDto.Code.TOKEN_INVALID));
    if(verificationToken.isExpired())
    throw new AppException(ErrorDto.Code.TOKEN_EXPIRED);
    UserEntity user = verificationToken.getUser();
    if(user.isVerified())
    throw new AppException(ErrorDto.Code.USER_IS_VERIFIED);
    user.verify();
    return user;
  }

  @Override
  public void deleteToken(VerificationTokenEntity verificationTokenEntity) {
    this.verificationTokenRepository.delete(verificationTokenEntity);
  }

  @Override
  public Optional<VerificationTokenEntity> findByToken(String token) {
    return this.verificationTokenRepository.findByToken(token);
  }

}
