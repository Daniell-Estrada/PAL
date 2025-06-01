package com.example.pal.repository;

import com.example.pal.model.StudentExamAttempt;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentExamAttemptRepository extends JpaRepository<StudentExamAttempt, Long> {

  // Para obtener los intentos de un estudiante específico para un examen específico
  List<StudentExamAttempt> findByStudentIdAndExamId(Long studentId, Long examId);

  // Para obtener el último intento de un estudiante para un examen (útil para GET
  // /api/exams/results/{examId})
  Optional<StudentExamAttempt> findFirstByStudentIdAndExamIdOrderBySubmissionDateDesc(
      Long studentId, Long examId);

  // Ejemplo de cómo cargar un intento con sus respuestas y preguntas (útil para evitar N+1)
  // Esto es para el endpoint de resultados
  @Query(
      "SELECT sea FROM StudentExamAttempt sea "
          + "LEFT JOIN FETCH sea.studentAnswers sa "
          + "LEFT JOIN FETCH sa.question q "
          + "LEFT JOIN FETCH sa.selectedOption "
          + // Asegúrate de cargar la opción seleccionada
          "LEFT JOIN FETCH q.options "
          + // Y todas las opciones de la pregunta para mostrarlas
          "WHERE sea.id = :attemptId AND sea.student.id = :studentId")
  Optional<StudentExamAttempt> findByIdAndStudentIdWithDetails(
      @Param("attemptId") Long attemptId, @Param("studentId") Long studentId);

  // Para el endpoint GET /api/exams/results/{examId} (obteniendo el último intento)
  @Query(
      "SELECT sea FROM StudentExamAttempt sea "
          + "LEFT JOIN FETCH sea.studentAnswers sa "
          + "LEFT JOIN FETCH sa.question q "
          + "LEFT JOIN FETCH sa.selectedOption "
          + "LEFT JOIN FETCH q.options "
          + "WHERE sea.student.id = :studentId AND sea.exam.id = :examId "
          + "ORDER BY sea.submissionDate DESC")
  List<StudentExamAttempt> findLatestByStudentIdAndExamIdWithDetails(
      @Param("studentId") Long studentId, @Param("examId") Long examId);
}
