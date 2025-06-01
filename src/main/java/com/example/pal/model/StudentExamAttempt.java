package com.example.pal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString; // Asegúrate de importar ToString

@Data
@Entity
@Table(name = "student_exam_attempts")
@EqualsAndHashCode(exclude = {"student", "exam", "studentAnswers"})
@ToString(exclude = {"student", "exam", "studentAnswers"}) // Añadir esta línea
public class StudentExamAttempt {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User student;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "exam_id", nullable = false)
  private Exam exam;

  @Column(nullable = false)
  private Double score; // Puntaje obtenido en este intento (ej. 85.0)

  @Column(nullable = false)
  private LocalDateTime submissionDate;

  // Respuestas dadas por el estudiante en este intento
  @OneToMany(mappedBy = "studentExamAttempt", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<StudentAnswer> studentAnswers;
}
