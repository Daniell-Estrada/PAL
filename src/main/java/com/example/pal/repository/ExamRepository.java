package com.example.pal.repository;

import com.example.pal.model.Exam;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
  // Para cargar un examen con sus preguntas y opciones (útil para iniciar un examen)
  @Query("SELECT DISTINCT e FROM Exam e LEFT JOIN FETCH e.questionsList WHERE e.id = :examId")
  Optional<Exam> findExamWithQuestionsById(@Param("examId") Long examId);
}
