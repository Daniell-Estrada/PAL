package com.example.pal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString; // Asegúrate de importar ToString

@Data
@Entity
@Table(name = "student_answers")
@EqualsAndHashCode(exclude = {"studentExamAttempt", "question", "selectedOption"})
@ToString(exclude = {"studentExamAttempt", "question", "selectedOption"}) // Añadir esta línea
public class StudentAnswer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "attempt_id", nullable = false)
  private StudentExamAttempt studentExamAttempt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "question_id", nullable = false)
  private Question question; // La pregunta que se respondió

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "selected_option_id",
      nullable = false) // Asumimos que siempre se selecciona una opción
  private Option selectedOption; // La opción que el estudiante seleccionó

  // Este campo se llenará durante la evaluación
  @Column(
      nullable =
          true) // Puede ser true, false. Null si aún no se evalúa (aunque la idea es evaluar al
                // instante)
  private Boolean isCorrect;
}
