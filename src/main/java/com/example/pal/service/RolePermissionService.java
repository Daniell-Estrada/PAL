package com.example.pal.service;

import com.example.pal.dto.rolepermission.RolePermissionDTO;
import com.example.pal.model.Permission;
import com.example.pal.model.Role;
import com.example.pal.repository.PermissionRepository;
import com.example.pal.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolePermissionService {
  @Autowired private PermissionRepository permissionRepository;
  @Autowired private RoleRepository roleRepository;
  @Autowired private ModelMapper modelMapper;

  public RolePermissionDTO createRolePermission(Long roleId, Long permissionId) {
    Role role = getRole(roleId);
    Permission permission = getPermission(permissionId);

    role.getPermissions().add(permission);
    permission.getRoles().add(role);

    roleRepository.save(role);
    permissionRepository.save(permission);

    return modelMapper.map(role, RolePermissionDTO.class);
  }

  public RolePermissionDTO getRolePermissions(Long roleId) {
    Role role = getRole(roleId);

    if (role.getPermissions().isEmpty()) {
      throw new RuntimeException("Role has no permissions");
    }

    return modelMapper.map(role, RolePermissionDTO.class);
  }

  public void deleteRolePermission(Long roleId, Long permissionId) {
    Role role = getRole(roleId);
    Permission permission = getPermission(permissionId);

    role.getPermissions().remove(permission);
    permission.getRoles().remove(role);

    roleRepository.save(role);
    permissionRepository.save(permission);
  }

  private Role getRole(Long roleId) {
    return roleRepository
        .findById(roleId)
        .orElseThrow(() -> new RuntimeException("Role not found"));
  }

  private Permission getPermission(Long permissionId) {
    return permissionRepository
        .findById(permissionId)
        .orElseThrow(() -> new RuntimeException("Permission not found"));
  }
}
