package com.ashimeru.personalfinance.demo_auth_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true, name = "username")
  @JsonProperty(value = "username")
  private String userName;
  private String password;
  private String email;
  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private UserRole role;
  @Column(nullable = false)
  private boolean verified;

  public boolean isVerified() {
    return verified;
  }

  public void verify() {
    this.verified = true;
  }
}
