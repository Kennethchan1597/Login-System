package com.ashimeru.personalfinance.demo_auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ashimeru.personalfinance.demo_auth_service.entity.UserEntity;
import com.ashimeru.personalfinance.demo_auth_service.repository.AuthRepository;
import com.ashimeru.personalfinance.demo_auth_service.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private AuthRepository authRepository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    if (username == null || username.isEmpty()) {
      throw new UsernameNotFoundException("Username cannot be null or empty");
    }
    UserEntity user = this.authRepository.findByUserName(username).
    orElseThrow( () -> new UsernameNotFoundException("Username Is Not Found"));
    return new CustomUserDetails(user);
  }
}
