package com.example.pal.service;

import com.example.pal.dto.user.*;
import com.example.pal.model.Role;
import com.example.pal.model.User;
import com.example.pal.repository.RoleRepository;
import com.example.pal.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired private UserRepository userRepository;

  @Autowired private RoleRepository roleRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private ModelMapper modelMapper;

  public UserDTO createUserWithRoles(CreateUserDTO userDTO) {
    User user = new User();
    user.setUsername(userDTO.getUsername());
    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

    Set<Role> roles = new HashSet<>();
    for (String roleName : userDTO.getRoles()) {
      Optional<Role> roleOpt = roleRepository.findByName(roleName);
      Role role =
          roleOpt.orElseGet(
              () -> {
                Role newRole = new Role();
                newRole.setName(roleName);
                return roleRepository.save(newRole);
              });
      roles.add(role);
    }

    user.setRoles(roles);
    User savedUser = userRepository.save(user);
    return modelMapper.map(savedUser, UserDTO.class);
  }

  public List<UserDTO> getAllUsers() {
    List<User> users = userRepository.findAll();
    return users.stream()
        .map(user -> modelMapper.map(user, UserDTO.class))
        .collect(Collectors.toList());
  }

  public Optional<UserDTO> getUserById(Long id) {
    return userRepository.findById(id).map(user -> modelMapper.map(user, UserDTO.class));
  }

  public UserDTO updateUser(Long id, UpdateUserDTO userDetails) {
    User user =
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));

    System.out.println(userDetails);
    user.setUsername(userDetails.getUsername());

    if (user.getPassword() != null && !user.getPassword().isEmpty()) {
      user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
    }

    if (userDetails.getRoleIds() != null && !userDetails.getRoleIds().isEmpty()) {
      Set<Role> roles = new HashSet<>();
      userDetails.getRoleIds().stream()
          .map(roleRepository::findById)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .forEach(roles::add);

      user.setRoles(roles);
    }

    User updatedUser = userRepository.save(user);

    return modelMapper.map(updatedUser, UserDTO.class);
  }

  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
