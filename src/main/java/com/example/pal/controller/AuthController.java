package com.example.pal.controller;

import com.example.pal.dto.auth.LoginRequestDTO;
import com.example.pal.dto.auth.LoginResponseDTO;
import com.example.pal.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
    LoginResponseDTO response = authService.login(loginRequest);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
    if (token != null && token.startsWith("Bearer ")) {
      token = token.substring(7);
      authService.logout(token);
    }
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/validate")
  public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
    if (token != null && token.startsWith("Bearer ")) {
      token = token.substring(7);
      boolean isValid = authService.validateToken(token);
      return ResponseEntity.ok(isValid);
    }
    return ResponseEntity.ok(false);
  }

  @GetMapping("/user-oauth")
  public String getUserFromOAuth(@AuthenticationPrincipal OAuth2User oAuth2User) {
    return "User: " + oAuth2User.getName() + ", Email: " + oAuth2User.getAttribute("email");
  }
}
