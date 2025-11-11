package com.ashimeru.login_system.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ashimeru.login_system.entity.UserEntity;

@Repository
public interface AuthRepository extends JpaRepository<UserEntity, Long>{
  Optional<UserEntity> findByUserName(String userName);
  Optional<UserEntity> findByEmail(String email);
}
