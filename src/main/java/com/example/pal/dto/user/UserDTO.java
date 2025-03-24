package com.example.pal.dto.user;

import com.example.pal.dto.role.RoleDTO;
import java.util.Set;
import lombok.Data;

@Data
public class UserDTO {
  private Long id;
  private String username;
  private Set<RoleDTO> roles;
}
