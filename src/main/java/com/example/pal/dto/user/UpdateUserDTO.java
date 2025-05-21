package com.example.pal.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Data;

@Data
public class UpdateUserDTO {

  @Size(min = 3, max = 30, message = "El nombre de usuario debe tener entre 3 y 30 caracteres")
  private String username;

  @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
  private String password;

  @Email(message = "El correo electrónico no es válido")
  private String email;

  private Set<Long> roleIds;
}