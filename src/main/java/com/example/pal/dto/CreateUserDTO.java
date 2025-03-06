package com.example.pal.dto;

import lombok.Data;

@Data
public class CreateUserDTO {
    private String username;
    private String password;
    private String[] roles;
}
