package com.ashimeru.login_system.security;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.ashimeru.login_system.dto.ErrorDto;
import com.ashimeru.login_system.exception.AppException;
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

    String path = request.getServletPath();

    // ✅ 1. Skip JWT validation for public endpoints
    if (path.startsWith("/auth/")) {
      filterChain.doFilter(request, response);
      return;
    }

    // ✅ 2. Skip if Authorization header is missing
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    // ✅ 3. Validate token for protected endpoints
    String token = authHeader.substring(7);
    try {
      Authentication authentication =
          SecurityContextHolder.getContext().getAuthentication();
      if (authentication == null) {
        Authentication result = this.jwtUtil.validate(token);
        SecurityContextHolder.getContext().setAuthentication(result);
      }
    } catch (JwtException | IllegalArgumentException e) {
      logger.warn("Invalid JWT: " + e.getMessage());
      throw new AppException(ErrorDto.Code.TOKEN_INVALID);
    }

    filterChain.doFilter(request, response);
  }
}
