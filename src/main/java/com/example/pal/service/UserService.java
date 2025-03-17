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

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private ModelMapper modelMapper;

  /**
   * Create a new user with roles. If the role does not exist, it will be created.
   * If the role already exists, it will be fetched from the database.
   * 
   * @param userDTO The user details that include the username, password, and
   *                roles.
   * @return The created user with roles, an instance of UserDTO.
   */
  public UserDTO createUserWithRoles(CreateUserDTO userDTO) {
    User user = new User();
    user.setUsername(userDTO.getUsername());
    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

    Set<Role> roles = new HashSet<>();
    for (String roleName : userDTO.getRoles()) {
      Optional<Role> roleOpt = roleRepository.findByName(roleName);
      Role role = roleOpt.orElseGet(
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

  /**
   * Get all users. Convert the user entities to UserDTOs
   * 
   * @return A list of all users, instances of UserDTO.
   */
  public List<UserDTO> getAllUsers() {
    List<User> users = userRepository.findAll();
    return users.stream()
        .map(user -> modelMapper.map(user, UserDTO.class))
        .collect(Collectors.toList());
  }

  /**
   * Get a user by ID.
   * 
   * @param id The ID of the user to fetch.
   * @return The user with the given ID, an instance of UserDTO.
   */
  public Optional<UserDTO> getUserById(Long id) {
    return userRepository.findById(id).map(user -> modelMapper.map(user, UserDTO.class));
  }

  /**
   * Update a user.
   * 
   * @param id          The ID of the user to update.
   * @param userDetails The updated user details.
   * @return The updated user, an instance of UserDTO.
   */
  public UserDTO updateUser(Long id, UpdateUserDTO userDetails) {
    User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));

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

  /**
   * Delete a user by ID.
   * 
   * @param id The ID of the user to delete.
   */
  public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new RuntimeException("User not found!");
    }
    userRepository.deleteById(id);
  }
}
