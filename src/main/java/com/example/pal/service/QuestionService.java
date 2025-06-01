package com.example.pal.service;

import com.example.pal.dto.exam.CreateOptionRequestDTO;
import com.example.pal.dto.exam.CreateQuestionRequestDTO;
import com.example.pal.dto.exam.OptionDetailResponseDTO;
import com.example.pal.dto.exam.QuestionDetailResponseDTO;
import com.example.pal.model.Exam;
import com.example.pal.model.Option;
import com.example.pal.model.Question;
import com.example.pal.repository.ExamRepository;
import com.example.pal.repository.OptionRepository;
import com.example.pal.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

  private final QuestionRepository questionRepository;
  private final ExamRepository examRepository;
  private final OptionRepository optionRepository;

  @Transactional
  public QuestionDetailResponseDTO addQuestionToExam(Long examId, CreateQuestionRequestDTO dto) {
    Exam exam =
        examRepository
            .findById(examId)
            .orElseThrow(
                () -> new EntityNotFoundException("Examen no encontrado con id: " + examId));

    Question question = new Question();
    question.setText(dto.getText());
    question.setExam(exam);
    // question.setOptions(new ArrayList<>());

    Question savedQuestion = questionRepository.save(question);
    return mapQuestionToQuestionDetailResponseDTO(savedQuestion);
  }

  @Transactional
  public OptionDetailResponseDTO addOptionToQuestion(Long questionId, CreateOptionRequestDTO dto) {
    Question question =
        questionRepository
            .findById(questionId)
            .orElseThrow(
                () -> new EntityNotFoundException("Pregunta no encontrada con id: " + questionId));

    Option option = new Option();
    option.setText(dto.getText());
    option.setCorrect(dto.getIsCorrect());
    option.setQuestion(question);

    Option savedOption = optionRepository.save(option);
    return mapOptionToOptionDetailResponseDTO(savedOption);
  }

  // Métodos de mapeo (pueden ser compartidos o estar en una clase Mapper)
  private QuestionDetailResponseDTO mapQuestionToQuestionDetailResponseDTO(Question question) {
    QuestionDetailResponseDTO dto = new QuestionDetailResponseDTO();
    dto.setId(question.getId());
    dto.setText(question.getText());
    if (question.getExam() != null) {
      dto.setExamId(question.getExam().getId());
    }
    // Al crear una pregunta, la lista de opciones estará vacía.
    // Si se carga una pregunta existente, se poblaría.
    dto.setOptions(
        question.getOptions() != null
            ? question.getOptions().stream()
                .map(this::mapOptionToOptionDetailResponseDTO)
                .collect(Collectors.toList())
            : Collections.emptyList());
    return dto;
  }

  private OptionDetailResponseDTO mapOptionToOptionDetailResponseDTO(Option option) {
    OptionDetailResponseDTO dto = new OptionDetailResponseDTO();
    dto.setId(option.getId());
    dto.setText(option.getText());
    dto.setCorrect(option.isCorrect());
    if (option.getQuestion() != null) {
      dto.setQuestionId(option.getQuestion().getId());
    }
    return dto;
  }
}
