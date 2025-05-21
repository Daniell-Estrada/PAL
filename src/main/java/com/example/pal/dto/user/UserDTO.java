package com.example.pal.dto.user;

import java.util.Set;

import com.example.pal.dto.role.RoleDTO;

import lombok.Data;

@Data
public class UserDTO {
  private Long id;
  private String username;
  private String email;
  private Set<RoleDTO> roles;
}
