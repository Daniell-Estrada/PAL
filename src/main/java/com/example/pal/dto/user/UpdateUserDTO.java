package com.example.pal.dto.user;

import java.util.Set;
import lombok.Data;

@Data
public class UpdateUserDTO {
  private String username;
  private String password;
  private Set<Long> roleIds;
}
