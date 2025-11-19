package com.ashimeru.login_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GooglePayload {
    private String googleUserId;
    private String idToken;
    private String email;
    private String name;
    private String photo;
}
