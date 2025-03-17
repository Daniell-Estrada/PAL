package com.example.pal.dto.user;

import lombok.Data;

@Data
public class CreateUserDTO {
  private String username;
  private String password;
  private String[] roles;
}
