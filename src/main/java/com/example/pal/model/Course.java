package com.example.pal.model;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
  private String difficulty; // básico, intermedio, avanzado

  @Column(nullable = false)
  private boolean free; // true = gratis (price == 0), false = pago (price > 0)

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now(); // fecha de creación

  @ManyToOne
  @JoinColumn(name = "instructor_id", nullable = false)
  private User instructor;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;
}
