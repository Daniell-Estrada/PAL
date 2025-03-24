package com.example.pal.controller;

import com.example.pal.dto.rolepermission.RolePermissionDTO;
import com.example.pal.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role-permissions")
public class RolePermissionController {
  @Autowired private RolePermissionService rolePermissionService;

  @PostMapping("/role/{roleId}/permission/{permissionId}")
  public ResponseEntity<RolePermissionDTO> createRolePermission(
      @PathVariable Long roleId, @PathVariable Long permissionId) {
    RolePermissionDTO rolePermission =
        rolePermissionService.createRolePermission(roleId, permissionId);
    return ResponseEntity.status(201).body(rolePermission);
  }

  @GetMapping("/role/{roleId}")
  public ResponseEntity<RolePermissionDTO> getRolePermissions(@PathVariable Long roleId) {
    RolePermissionDTO rolePermission = rolePermissionService.getRolePermissions(roleId);
    return ResponseEntity.ok(rolePermission);
  }

  @DeleteMapping("/role/{roleId}/permission/{permissionId}")
  public ResponseEntity<Void> deleteRolePermission(
      @PathVariable Long roleId, @PathVariable Long permissionId) {
    rolePermissionService.deleteRolePermission(roleId, permissionId);
    return ResponseEntity.noContent().build();
  }
}
