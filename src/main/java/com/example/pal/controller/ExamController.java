package com.example.pal.controller;

import com.example.pal.dto.exam.CreateExamRequestDTO;
import com.example.pal.dto.exam.CreateQuestionRequestDTO;
import com.example.pal.dto.exam.ExamDetailResponseDTO;
import com.example.pal.dto.exam.ExamResultDetailsDTO;
import com.example.pal.dto.exam.ExamSubmissionDTO;
import com.example.pal.dto.exam.ExamSubmissionResultDTO;
import com.example.pal.dto.exam.QuestionDetailResponseDTO;
import com.example.pal.service.ExamService;
import com.example.pal.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exams") // Ruta base para las operaciones de exámenes
@RequiredArgsConstructor
public class ExamController {

  private final ExamService examService;
  private final QuestionService questionService;

  @PostMapping("/submit/{examId}")
  public ResponseEntity<ExamSubmissionResultDTO> submitExam(
      @PathVariable Long examId, @Valid @RequestBody ExamSubmissionDTO submissionDTO) {
    // En una aplicación real con Spring Security, studentId vendría del Principal (usuario
    // autenticado).
    // Por ahora, es parte del DTO. Considera validar si submissionDTO.getStudentId() coincide con
    // el usuario autenticado.
    ExamSubmissionResultDTO result = examService.submitExam(examId, submissionDTO);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(result); // CREATED (201) es apropiado para envíos exitosos
  }

  @GetMapping("/results/{examId}")
  public ResponseEntity<ExamResultDetailsDTO> getExamResults(
      @PathVariable Long examId,
      @RequestParam
          Long studentId) { // studentId también podría derivarse del Principal autenticado
    // Aquí, studentId se pasa como parámetro de consulta.
    // En un sistema con autenticación, obtendrías el ID del estudiante del contexto de seguridad.
    ExamResultDetailsDTO results = examService.getExamResults(examId, studentId);
    return ResponseEntity.ok(results);
  }

  @PostMapping("/create")
  public ResponseEntity<ExamDetailResponseDTO> createExam(
      @Valid @RequestBody CreateExamRequestDTO createExamRequestDTO) {
    ExamDetailResponseDTO createdExam = examService.createExam(createExamRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdExam);
  }

  @PostMapping("/{examId}/questions")
  public ResponseEntity<QuestionDetailResponseDTO> addQuestionToExam(
      @PathVariable Long examId,
      @Valid @RequestBody CreateQuestionRequestDTO createQuestionRequestDTO) {
    QuestionDetailResponseDTO createdQuestion =
        questionService.addQuestionToExam(examId, createQuestionRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
  }
}
