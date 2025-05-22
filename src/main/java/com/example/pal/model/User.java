package com.example.pal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Email(message = "El correo electrónico no es válido")
  @Column(nullable = false, unique = true)
  private String email;

  @NotNull(message = "El usuario debe tener al menos un rol")
  @Size(min = 1, message = "El usuario debe tener al menos un rol")
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  @JsonIgnore
  private Set<Role> roles;

  @OneToMany(mappedBy = "student")
  @JsonIgnore
  private Set<Enrollment> enrollments;

  @OneToMany(mappedBy = "user")
  private Set<Score> scores;
}
