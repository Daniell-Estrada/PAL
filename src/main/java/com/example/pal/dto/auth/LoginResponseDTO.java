package com.example.pal.dto.auth;

import com.example.pal.dto.user.UserDTO;
import lombok.Data;

@Data
public class LoginResponseDTO {
  private String token;
  private String redirectUrl;
  private UserDTO user;

  public static LoginResponseDTO fromUserDTO(UserDTO userDTO, String token) {
    LoginResponseDTO response = new LoginResponseDTO();
    response.setUser(userDTO);
    response.setToken(token);

    String redirectUrl = "/dashboard"; // URL por defecto

    if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
      boolean isAdmin =
          userDTO.getRoles().stream().anyMatch(role -> "ADMIN".equals(role.getName()));

      boolean isInstructor =
          userDTO.getRoles().stream().anyMatch(role -> "INSTRUCTOR".equals(role.getName()));

      if (isAdmin) {
        redirectUrl = "/admin";
      } else if (isInstructor) {
        redirectUrl = "/instructor/dashboard";
      } else {
        redirectUrl = "/student/dashboard";
      }
    }

    response.setRedirectUrl(redirectUrl);
    return response;
  }
}
