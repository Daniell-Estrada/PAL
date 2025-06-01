package com.example.pal.model;

import jakarta.persistence.*;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString; // Asegúrate de importar ToString

@Data
@Entity
@Table(name = "options")
@EqualsAndHashCode(
    exclude = {"question", "studentAnswers"}) // studentAnswers también si existe y es bidireccional
@ToString(
    exclude = {"question", "studentAnswers"}) // studentAnswers también si existe y es bidireccional
public class Option {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String text; // Texto de la opción, ej: "Bogotá"

  @Column(nullable = false)
  private boolean isCorrect; // true si esta es la opción correcta

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "question_id", nullable = false)
  private Question question; // Opción asociada a una pregunta

  // Considera si tienes una relación bidireccional con StudentAnswer
  @OneToMany(mappedBy = "selectedOption")
  private Set<StudentAnswer> studentAnswers;
}
