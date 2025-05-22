package com.example.pal.service;

import com.example.pal.dto.auth.LoginRequestDTO;
import com.example.pal.dto.auth.LoginResponseDTO;
import com.example.pal.dto.user.UserDTO;
import com.example.pal.model.User;
import com.example.pal.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private ModelMapper modelMapper;

  // Simulación simple de tokens (en producción usaríamos JWT)
  private Map<String, Long> activeTokens = new HashMap<>();

  public LoginResponseDTO login(LoginRequestDTO loginRequest) {
    User user = userRepository.findByUsername(loginRequest.getUsername());

    if (user == null) {
      throw new BadCredentialsException("Usuario o contraseña incorrectos");
    }

    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      throw new BadCredentialsException("Usuario o contraseña incorrectos");
    }

    // Generar token simple (en producción usaríamos JWT)
    String token = UUID.randomUUID().toString();
    activeTokens.put(token, user.getId());

    // Convertir a DTO y devolver respuesta
    UserDTO userDTO = modelMapper.map(user, UserDTO.class);
    return LoginResponseDTO.fromUserDTO(userDTO, token);
  }

  public void logout(String token) {
    activeTokens.remove(token);
  }

  public Long getUserIdFromToken(String token) {
    return activeTokens.get(token);
  }

  public boolean validateToken(String token) {
    return activeTokens.containsKey(token);
  }
}
