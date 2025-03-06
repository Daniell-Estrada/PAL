package com.example.pal.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pal.dto.CreateUserDTO;
import com.example.pal.dto.UserDTO;
import com.example.pal.model.Role;
import com.example.pal.model.User;
import com.example.pal.repository.RoleRepository;
import com.example.pal.repository.UserRepository;

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

    public User createUserWithRoles(CreateUserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        Set<Role> roles = new HashSet<>();
        for (String roleName : userDTO.getRoles()) {
            Optional<Role> roleOpt = roleRepository.findByName(roleName);
            Role role = roleOpt.orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName(roleName);
                return roleRepository.save(newRole);
            });
            roles.add(role);
        }

        user.setRoles(roles);
        return userRepository.save(user);
    }

    
    public List<UserDTO> getAllUsers() {
    	List<User> users = userRepository.findAll();
    	return users.stream().map(user->modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
    			
    }
    
    public Optional<User> getUserById(Long id){
    	return userRepository.findById(id);
    }
    
    public User updateUser(Long id, User userDetails) {
    	User user = userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found!"));
    	user.setUsername(userDetails.getUsername());
    	if(user.getPassword()!=null) {
    		user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
    	}
    	user.setRoles(userDetails.getRoles());
    	return userRepository.save(user);
    }
    
    public void deleteUser(Long id) {
    	userRepository.deleteById(id);
    }
}
