package com.example.pal.service;

import com.example.pal.dto.permission.CreatePermissionDTO;
import com.example.pal.dto.permission.PermissionDTO;
import com.example.pal.model.Permission;
import com.example.pal.repository.PermissionRepository;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {
  @Autowired private PermissionRepository permissionRepository;
  @Autowired private ModelMapper modelMapper;

  public PermissionDTO createPermission(CreatePermissionDTO permissionDTO) {
    Permission permission = modelMapper.map(permissionDTO, Permission.class);
    Permission savedPermission = permissionRepository.save(permission);
    return modelMapper.map(savedPermission, PermissionDTO.class);
  }

  public List<PermissionDTO> findAllPermissions() {
    return permissionRepository.findAll().stream()
        .map(permission -> modelMapper.map(permission, PermissionDTO.class))
        .toList();
  }

  public void deletePermission(Long id) {
    permissionRepository.deleteById(id);
  }
}
