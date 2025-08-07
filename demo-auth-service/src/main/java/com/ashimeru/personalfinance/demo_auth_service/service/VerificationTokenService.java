package com.ashimeru.personalfinance.demo_auth_service.service;

import java.util.Optional;
import com.ashimeru.personalfinance.demo_auth_service.entity.UserEntity;
import com.ashimeru.personalfinance.demo_auth_service.entity.VerificationTokenEntity;

public interface VerificationTokenService {
  void saveTokenForUser(String token, UserEntity userEntity);

  VerificationTokenEntity save(VerificationTokenEntity verificationTokenEntity);

  UserEntity VerifyUser(String token);

  void deleteToken(VerificationTokenEntity verificationTokenEntity);

  Optional<VerificationTokenEntity> findByToken(String token);
}
