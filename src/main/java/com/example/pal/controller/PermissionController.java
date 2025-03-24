package com.example.pal.controller;

import com.example.pal.dto.permission.CreatePermissionDTO;
import com.example.pal.dto.permission.PermissionDTO;
import com.example.pal.service.PermissionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
  @Autowired private PermissionService permissionService;

  @GetMapping("/all")
  public ResponseEntity<List<PermissionDTO>> findAll() {
    return ResponseEntity.ok(permissionService.findAllPermissions());
  }

  @PostMapping("/create")
  public ResponseEntity<PermissionDTO> create(@ModelAttribute CreatePermissionDTO permissionDTO) {
    PermissionDTO permission = permissionService.createPermission(permissionDTO);
    return ResponseEntity.status(201).body(permission);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    permissionService.deletePermission(id);
    return ResponseEntity.noContent().build();
  }
}
