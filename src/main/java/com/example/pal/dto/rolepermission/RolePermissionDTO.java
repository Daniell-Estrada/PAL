package com.example.pal.dto.rolepermission;

import com.example.pal.dto.permission.PermissionDTO;
import java.util.Set;
import lombok.Data;

@Data
public class RolePermissionDTO {
  private Long id;
  private String name;
  private Set<PermissionDTO> permissions;
}
