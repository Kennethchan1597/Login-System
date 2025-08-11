package com.ashimeru.personalfinance.demo_auth_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import com.ashimeru.personalfinance.demo_auth_service.security.JwtAuthenticationFilter;
import com.ashimeru.personalfinance.demo_auth_service.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class AppConfig {

   @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(c -> c.disable())
        .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**").permitAll()
        .requestMatchers("/dashborad").hasRole("ADMIN")
            .anyRequest().authenticated())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration c) throws Exception{
    return c.getAuthenticationManager();
  }

}
