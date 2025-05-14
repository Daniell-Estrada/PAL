package com.example.pal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

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

  @ManyToOne
  @JoinColumn(name = "instructor_id", nullable = false)
  private User instructor;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;
}
