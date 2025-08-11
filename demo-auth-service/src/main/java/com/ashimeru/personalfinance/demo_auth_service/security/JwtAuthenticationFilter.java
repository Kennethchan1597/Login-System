package com.ashimeru.personalfinance.demo_auth_service.security;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.ashimeru.personalfinance.demo_auth_service.dto.ErrorDto.Code;
import com.ashimeru.personalfinance.demo_auth_service.exception.AppException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);
      try {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          if (authentication == null) {
            Authentication result = this.jwtUtil.validate(token);
            SecurityContextHolder.getContext().setAuthentication(result);
        }
      } catch (JwtException | IllegalArgumentException e) {
        logger.warn("Invalid JWT: " + e.getMessage());
        throw new AppException(Code.TOKEN_INVALID);
      }
    }
    filterChain.doFilter(request, response);
  }
}
