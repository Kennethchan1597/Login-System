package com.ashimeru.personalfinance.demo_auth_service.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ashimeru.personalfinance.demo_auth_service.entity.VerificationTokenEntity;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, Long>{
  Optional<VerificationTokenEntity> findByToken(String token);
}

