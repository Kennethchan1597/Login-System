package com.ashimeru.personalfinance.demo_auth_service.security;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.ashimeru.personalfinance.demo_auth_service.dto.ErrorDto.Code;
import com.ashimeru.personalfinance.demo_auth_service.dto.UserDto;
import com.ashimeru.personalfinance.demo_auth_service.exception.AppException;
import com.ashimeru.personalfinance.demo_auth_service.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
  @Value("${jwt.secretkey}")
  private String SECRET_KEY;
  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  public String generateToken(UserDto user) {
    Date expTime = new Date(System.currentTimeMillis() + 1000 * 60 * 30);
    Map<String, Object> claims = new HashMap<>();
    claims.put("userName", user.getUserName());
    claims.put("id", user.getId());
    claims.put("email", user.getEmail());
    claims.put("role", user.getRole());

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(user.getUserName())
        .setIssuedAt(new Date())
        .setExpiration(expTime)
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();
  }

  public String extractUsername(String token) {
    if (token == null || !this.isTokenValid(token))
    throw new AppException(Code.TOKEN_INVALID);
    return Jwts.parser().setSigningKey(this.SECRET_KEY).parseClaimsJws(token)
        .getBody().getSubject();
  }

  public String extractRole(String token) {
    if (token == null || !this.isTokenValid(token))
    throw new AppException(Code.TOKEN_INVALID);
    Claims claims = Jwts.parser().setSigningKey(this.SECRET_KEY).parseClaimsJws(token)
        .getBody();
    return claims.get("role", String.class);
  }

  public boolean isTokenValid(String token) {
    try {
      Jwts.parser().setSigningKey(this.SECRET_KEY).parseClaimsJws(token); 
      return true;
    } catch (ExpiredJwtException e) {
      throw new AppException(Code.TOKEN_EXPIRED);
    } catch (JwtException e) {
      throw new AppException(Code.TOKEN_INVALID);
    }
  }

  public Authentication validate(String token) {
    if (token == null || !this.isTokenValid(token))
      throw new AppException(Code.TOKEN_INVALID);
      String username = this.extractUsername(token);
      String role = this.extractRole(token);
        List<GrantedAuthority> authorities = Collections.singletonList(
        new SimpleGrantedAuthority("ROLE_" + role)
    );
      UserDetails user = User.builder()
      .username(username)
      .password("")  // no password needed here
      .authorities(authorities)
      .build();
      return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

}
