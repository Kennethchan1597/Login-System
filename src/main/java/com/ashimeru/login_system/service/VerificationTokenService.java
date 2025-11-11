package com.ashimeru.login_system.service;

import java.util.Optional;
import com.ashimeru.login_system.entity.UserEntity;
import com.ashimeru.login_system.entity.VerificationTokenEntity;

public interface VerificationTokenService {
  void saveTokenForUser(String token, UserEntity userEntity);

  VerificationTokenEntity save(VerificationTokenEntity verificationTokenEntity);

  UserEntity VerifyUser(String token);

  void deleteToken(VerificationTokenEntity verificationTokenEntity);

  Optional<VerificationTokenEntity> findByToken(String token);
}
