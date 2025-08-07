package com.ashimeru.personalfinance.demo_auth_service.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.ashimeru.personalfinance.demo_auth_service.entity.UserEntity;


public class CustomUserDetails implements UserDetails {

  private final UserEntity user;

  public CustomUserDetails(UserEntity user) {
      this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
  }

  @Override
  public String getPassword() {
      return user.getPassword();
  }

  @Override
  public String getUsername() {
      return user.getUserName();
  }

  @Override
  public boolean isAccountNonExpired() {
      return true;
  }

  @Override
  public boolean isAccountNonLocked() {
      return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
      return true;
  }

  @Override
  public boolean isEnabled() {
      return user.isVerified();
  }

  public UserEntity getUserEntity() {
      return user;
  }
}
