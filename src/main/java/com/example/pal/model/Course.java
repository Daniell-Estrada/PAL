package com.example.pal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "courses")
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @NotNull(message = "El curso debe tener precio")
  @PositiveOrZero(message = "El precio del curso no puede ser negativo")
  @Column(nullable = false)
  private double price;

  @Column(nullable = false)
  private String difficulty; // básico, intermedio, avanzado

  @Column(nullable = false)
  private boolean free; // true = gratis (price == 0), false = pago (price > 0)

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now(); // fecha de creación

  @OneToMany(mappedBy = "course")
  private Set<Content> contents;

  @OneToMany(mappedBy = "course")
  private Set<Forum> forums;

  @OneToMany(mappedBy = "course")
  private Set<Exam> exams;

  @OneToMany(mappedBy = "course")
  private Set<Enrollment> enrollments;

  @OneToMany(mappedBy = "course")
  private Set<Report> reports;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @ManyToOne
  @JoinColumn(name = "instructor_id", nullable = false)
  private User instructor;
}
