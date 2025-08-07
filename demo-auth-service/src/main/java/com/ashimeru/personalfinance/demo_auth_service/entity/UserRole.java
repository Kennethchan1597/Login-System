package com.ashimeru.personalfinance.demo_auth_service.entity;


public enum UserRole {
  ADMIN(1), USER(2);

  private int value;

  private UserRole(int value) {
      this.value = value;
    }

  public int getValue() {
    return this.value;
  }
}
