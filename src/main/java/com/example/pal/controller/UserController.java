package com.example.pal.controller;

import com.example.pal.dto.user.CreateUserDTO;
import com.example.pal.dto.user.UpdateUserDTO;
import com.example.pal.dto.user.UserDTO;
import com.example.pal.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.pal.model.User;


@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired private UserService userService;

  @PostMapping("/create")
  public ResponseEntity<UserDTO> createUser(@ModelAttribute CreateUserDTO userDTO) {
    UserDTO user = userService.createUserWithRoles(userDTO);
    return ResponseEntity.status(201).body(user);
  }

  @GetMapping("/all")
  public ResponseEntity<List<UserDTO>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
    Optional<UserDTO> user = userService.getUserById(id);
    return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<UserDTO> updateUser(
      @PathVariable Long id, @RequestBody UpdateUserDTO userDetails) {
    return ResponseEntity.ok(userService.updateUser(id, userDetails));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/by-role")
    public ResponseEntity<List<User>> getUsersByRole(@RequestParam String roleName) {
    	List<User> users = userService.getUsersByRole(roleName);
    	if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.ok(users);
    }
}
