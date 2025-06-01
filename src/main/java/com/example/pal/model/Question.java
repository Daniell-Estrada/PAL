package com.example.pal.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString; // Asegúrate de importar ToString

@Data
@Entity
@Table(name = "questions")
@EqualsAndHashCode(
    exclude = {
      "options",
      "exam",
      "studentAnswers"
    }) // studentAnswers también si existe y es bidireccional
@ToString(
    exclude = {
      "options",
      "exam",
      "studentAnswers"
    }) // studentAnswers también si existe y es bidireccional
public class Question {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String text; // Texto de la pregunta, ej: "¿Cuál es la capital de Colombia?"

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "exam_id", nullable = false)
  private Exam exam; // Pregunta asociada a un examen

  // Opciones de la pregunta
  @OneToMany(
      mappedBy = "question",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private Set<Option> options = new HashSet<>();

  // Considera si tienes una relación bidireccional con StudentAnswer
  @OneToMany(mappedBy = "question")
  private Set<StudentAnswer> studentAnswers;
}
