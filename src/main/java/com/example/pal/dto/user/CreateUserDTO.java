package com.example.pal.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserDTO {

  @NotBlank(message = "El nombre de usuario no puede estar vacío")
  @Size(min = 3, max = 30, message = "El nombre de usuario debe tener entre 3 y 30 caracteres")
  private String username;

  @NotBlank(message = "La contraseña no puede estar vacía")
  @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
  private String password;

  @NotBlank(message = "El correo electrónico no puede estar vacío")
  @Email(message = "El correo electrónico no es válido")
  private String email;

  @NotEmpty(message = "El usuario debe tener al menos un rol")
  private String[] roles;
}