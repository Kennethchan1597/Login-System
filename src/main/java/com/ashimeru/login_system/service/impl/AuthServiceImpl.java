package com.ashimeru.login_system.service.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.ashimeru.login_system.dto.ApplePayload;
import com.ashimeru.login_system.dto.DtoMapper;
import com.ashimeru.login_system.dto.ErrorDto;
import com.ashimeru.login_system.dto.GooglePayload;
import com.ashimeru.login_system.dto.LoginDto;
import com.ashimeru.login_system.dto.SignUpDto;
import com.ashimeru.login_system.dto.UserDto;
import com.ashimeru.login_system.entity.EntityMapper;
import com.ashimeru.login_system.entity.UserEntity;
import com.ashimeru.login_system.entity.UserRole;
import com.ashimeru.login_system.exception.AppException;
import com.ashimeru.login_system.repository.AuthRepository;
import com.ashimeru.login_system.security.CustomUserDetails;
import com.ashimeru.login_system.security.oauth.AppleJwtService;
import com.ashimeru.login_system.security.oauth.GoogleAuthService;
import com.ashimeru.login_system.service.AuthService;
import com.ashimeru.login_system.service.EmailService;
import com.ashimeru.login_system.service.VerificationTokenService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.mail.MessagingException;

@Service
public class AuthServiceImpl implements AuthService {
  @Autowired
  private AuthRepository authRepository;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private EntityMapper entityMapper;
  @Autowired
  private DtoMapper dtoMapper;
  @Autowired
  private VerificationTokenService verificationTokenService;
  @Autowired
  private EmailService emailService;
  @Autowired
  private AppleJwtService appleJwtService;
  @Value(value = "${apple.client.id}")
  private String appleClientId;
  @Autowired
  private GoogleAuthService googleAuthService;
  

  @Override
  public Optional<UserEntity> findByUserName(String name) {
    return this.authRepository.findByUserName(name);
  }

  @Override
  public Optional<UserEntity> findByEmail(String email) {
    return this.authRepository.findByEmail(email);
  }

  @Override
  public UserEntity saveUser(UserEntity user) {
    return this.authRepository.save(Objects.requireNonNull(user));
  }

  @Override
  public void register(SignUpDto signUpDto) {
    if (this.findByEmail(signUpDto.getEmail()).isPresent())
      throw new AppException(ErrorDto.Code.EMAIL_EXISTED);
    if (this.findByUserName(signUpDto.getUserName()).isPresent())
      throw new AppException(ErrorDto.Code.USER_EXISTED);
    UserEntity entity = this.entityMapper.map(signUpDto);
    String token = UUID.randomUUID().toString();
    String url = "http://localhost:8090/auth/verify?token=" + token;
    try {
      this.emailService.sendVerificationEmail(entity.getEmail(), url);
      this.saveUser(entity);
      this.verificationTokenService.saveTokenForUser(token, entity);
    } catch (MessagingException e) {
      throw new AppException(ErrorDto.Code.EMAIL_SEND_FAILED);
    }
  }

  @Override
  public UserDto login(LoginDto loginDto) {
    this.findByUserName(loginDto.getUsername()).orElseThrow(() -> new AppException(ErrorDto.Code.USER_NOT_FOUND));
    if (loginDto.getPassword() == null || loginDto.getPassword().isEmpty()) {
      throw new AppException(ErrorDto.Code.INVALID_PASSWORD);
    }
    try {
      Authentication auth = this.authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
              loginDto.getPassword()));
      CustomUserDetails userd = (CustomUserDetails) auth.getPrincipal();
      UserEntity user = userd.getUserEntity();
      return this.dtoMapper.map(user);
    } catch (BadCredentialsException e) {
      throw new AppException(ErrorDto.Code.WRONG_PASSWORD);
    }
  }

  @Override
  public UserDto loginWithApple(ApplePayload payload) throws Exception{
    
    JWTClaimsSet claims = this.appleJwtService
      .verifyAndDecode(payload.getIdentityToken(), appleClientId);
    String appleId = claims.getSubject();
    String email = claims.getStringClaim("email");
    Optional<UserEntity> existingUser = this.authRepository.findByAppleId(appleId);
    if (existingUser.isPresent()) {
      System.out.println("Subsequent Login");
      return this.dtoMapper.map(existingUser.get());
    }
    
    String givenName = payload.getFullName().getGivenName();
    String familyName = payload.getFullName().getFamilyName();
    String fullName;
    if (givenName != null && familyName != null) {
      fullName = givenName + " " + familyName; // add a space
    } else if (givenName != null) {
      fullName = givenName;
    } else if (familyName != null) {
      fullName = familyName;
    } else {
      fullName = "please enter your name";
    }

    UserEntity userEntity = UserEntity.builder()
      .email(email != null ? email : null)
      .appleId(appleId)
      .googleId(null)
      .userName(fullName)
      .password(null)
      .role(UserRole.USER)
      .build();
      
    this.authRepository.save(userEntity);
    System.out.println("First Login");
    return this.dtoMapper.map(userEntity);
  }

  @Override
  public UserDto loginWithGoogle(GooglePayload payloadDto) {
      GoogleIdToken.Payload payload = this.googleAuthService
        .verifyGoogleIdToken(payloadDto.getIdToken());
      if (payload == null) {
        throw new RuntimeException("Invalid Google token");
    }
    String googleId = payload.getSubject();
    String email = payload.getEmail();
    String name = payloadDto.getName();

    Optional<UserEntity> exisingUser = this.authRepository.findByGoogleId(googleId);

    if(exisingUser.isPresent()) {
      System.out.println("Subsequent Login");
      return this.dtoMapper.map(exisingUser.get());
    }

    UserEntity userEntity = UserEntity.builder()
    .email(email != null ? email : null)
    .appleId(null)
    .googleId(googleId)
    .userName(name != null ? name : "Please enter your name")
    .password(null)
    .role(UserRole.USER)
    .build();
    
    this.authRepository.save(userEntity);
    System.out.println("First Login");
    return this.dtoMapper.map(userEntity);
  }
}
