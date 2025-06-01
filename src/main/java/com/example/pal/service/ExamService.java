package com.example.pal.service;

import com.example.pal.dto.exam.*;
import com.example.pal.model.*;
import com.example.pal.repository.*;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections; // Para lista vacía
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamService {

  private final ExamRepository examRepository;
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final StudentExamAttemptRepository studentExamAttemptRepository;

  @Transactional
  public ExamSubmissionResultDTO submitExam(Long examId, ExamSubmissionDTO submissionDTO) {
    User student =
        userRepository
            .findById(submissionDTO.getStudentId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Student not found with id: " + submissionDTO.getStudentId()));

    Exam exam =
        examRepository
            .findExamWithQuestionsById(examId)
            .orElseThrow(() -> new EntityNotFoundException("Exam not found with id: " + examId));

    StudentExamAttempt attempt = new StudentExamAttempt();
    attempt.setStudent(student);
    attempt.setExam(exam);
    attempt.setSubmissionDate(LocalDateTime.now());

    List<StudentAnswer> studentAnswers = new ArrayList<>();
    int correctAnswersCount = 0;

    Map<Long, Question> examQuestionsMap =
        exam.getQuestionsList().stream()
            .collect(Collectors.toMap(Question::getId, Function.identity()));

    for (StudentAnswerSubmissionDTO answerDTO : submissionDTO.getAnswers()) {
      Question question = examQuestionsMap.get(answerDTO.getQuestionId());
      if (question == null) {
        System.err.println(
            "Advertencia: Pregunta con ID "
                + answerDTO.getQuestionId()
                + " no encontrada en el examen o no cargada correctamente.");
        continue;
      }

      Option selectedOption =
          question.getOptions().stream()
              .filter(opt -> opt.getId().equals(answerDTO.getSelectedOptionId()))
              .findFirst()
              .orElseThrow(
                  () ->
                      new EntityNotFoundException(
                          "Selected option not found for question id: " + question.getId()));

      StudentAnswer studentAnswer = new StudentAnswer();
      studentAnswer.setStudentExamAttempt(attempt);
      studentAnswer.setQuestion(question);
      studentAnswer.setSelectedOption(selectedOption);
      studentAnswer.setIsCorrect(selectedOption.isCorrect());

      if (selectedOption.isCorrect()) {
        correctAnswersCount++;
      }
      studentAnswers.add(studentAnswer);
    }

    attempt.setStudentAnswers(studentAnswers);
    double score = 0;
    if (!exam.getQuestionsList().isEmpty()) {
      score = ((double) correctAnswersCount / exam.getQuestionsList().size()) * 100;
    }
    attempt.setScore(score);

    StudentExamAttempt savedAttempt = studentExamAttemptRepository.save(attempt);
    // studentAnswerRepository.saveAll(studentAnswers); // Not needed if cascade is set up correctly
    // on StudentExamAttempt.studentAnswers

    return new ExamSubmissionResultDTO(
        savedAttempt.getId(),
        savedAttempt.getScore(),
        exam.getQuestionsList().size(),
        correctAnswersCount,
        "Exam submitted successfully.");
  }

  @Transactional(readOnly = true)
  public ExamResultDetailsDTO getExamResults(Long examId, Long studentId) {
    // Use the specific query to get the latest attempt with all details
    List<StudentExamAttempt> attempts =
        studentExamAttemptRepository.findLatestByStudentIdAndExamIdWithDetails(studentId, examId);

    if (attempts.isEmpty()) {
      throw new EntityNotFoundException(
          "No exam attempts found for exam " + examId + " by student " + studentId);
    }
    StudentExamAttempt latestAttempt = attempts.get(0); // The query orders by submissionDate DESC

    ExamResultDetailsDTO resultDTO = new ExamResultDetailsDTO();
    resultDTO.setAttemptId(latestAttempt.getId());
    resultDTO.setExamId(latestAttempt.getExam().getId());
    resultDTO.setExamTitle(latestAttempt.getExam().getTitle());
    resultDTO.setStudentId(latestAttempt.getStudent().getId());
    resultDTO.setStudentUsername(latestAttempt.getStudent().getUsername());
    resultDTO.setScore(latestAttempt.getScore());
    resultDTO.setSubmissionDate(latestAttempt.getSubmissionDate());

    List<AnswerDetailDTO> answerDetails = new ArrayList<>();
    int correctCount = 0;
    int totalQuestions = 0;

    if (latestAttempt.getExam().getQuestionsList() != null) {
      totalQuestions =
          latestAttempt
              .getExam()
              .getQuestionsList()
              .size(); // Total questions in the exam definition
    }

    Map<Long, StudentAnswer> studentAnswersMap =
        latestAttempt.getStudentAnswers().stream()
            .collect(
                Collectors.toMap(
                    sa -> sa.getQuestion().getId(), // Key mapper: Question ID
                    Function.identity(), // Value mapper: the StudentAnswer object itself
                    (existingValue, newValue) ->
                        existingValue // Merge function: if duplicate keys, keep the existing value
                    ));

    // Iterate over questions of the exam to ensure all questions are represented
    // The query `findLatestByStudentIdAndExamIdWithDetails` should fetch q.options
    for (Question question : latestAttempt.getExam().getQuestionsList()) {
      AnswerDetailDTO ad = new AnswerDetailDTO();
      ad.setQuestionId(question.getId());
      ad.setQuestionText(question.getText());

      StudentAnswer studentAnswer = studentAnswersMap.get(question.getId());
      if (studentAnswer != null) {
        ad.setSelectedOptionId(studentAnswer.getSelectedOption().getId());
        ad.setSelectedOptionText(studentAnswer.getSelectedOption().getText());
        ad.setWasCorrect(studentAnswer.getIsCorrect());
        if (Boolean.TRUE.equals(studentAnswer.getIsCorrect())) {
          correctCount++;
        }
      } else {
        // Question was not answered by the student
        ad.setSelectedOptionId(null);
        ad.setSelectedOptionText("Not Answered");
        ad.setWasCorrect(false);
      }

      List<OptionDetailDTO> optionDTOs =
          question.getOptions().stream()
              .map(
                  opt -> {
                    OptionDetailDTO od = new OptionDetailDTO();
                    od.setId(opt.getId());
                    od.setText(opt.getText());
                    od.setCorrect(opt.isCorrect());
                    od.setSelectedByStudent(
                        studentAnswer != null
                            && opt.getId().equals(studentAnswer.getSelectedOption().getId()));
                    return od;
                  })
              .collect(Collectors.toList());
      ad.setOptions(optionDTOs);
      answerDetails.add(ad);
    }

    resultDTO.setTotalQuestions(totalQuestions);
    resultDTO.setCorrectAnswersCount(
        correctCount); // This count is from the actual answers in the attempt
    resultDTO.setAnswers(answerDetails);
    resultDTO.setGeneralComments(
        "Review your answers below. Your score is "
            + String.format("%.2f", latestAttempt.getScore())
            + "%.");

    return resultDTO;
  }

  @Transactional
  public ExamDetailResponseDTO createExam(CreateExamRequestDTO dto) {
    Course course =
        courseRepository
            .findById(dto.getCourseId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Curso no encontrado con id: " + dto.getCourseId()));

    Exam exam = new Exam();
    exam.setTitle(dto.getTitle());
    exam.setCourse(course);
    // exam.setQuestionsList(new ArrayList<>()); // JPA inicializará si es necesario o se puede
    // hacer aquí

    Exam savedExam = examRepository.save(exam);
    return mapExamToExamDetailResponseDTO(savedExam);
  }

  // Método de mapeo (puedes tener una clase Mapper dedicada para esto)
  private ExamDetailResponseDTO mapExamToExamDetailResponseDTO(Exam exam) {
    ExamDetailResponseDTO dto = new ExamDetailResponseDTO();
    dto.setId(exam.getId());
    dto.setTitle(exam.getTitle());
    if (exam.getCourse() != null) {
      dto.setCourseId(exam.getCourse().getId());
      dto.setCourseTitle(exam.getCourse().getTitle()); // Asumiendo que Course tiene getTitle()
    }
    // Si el examen se acaba de crear, questionsList podría ser null o vacía.
    // Si se carga un examen existente, se poblaría.
    dto.setQuestions(
        exam.getQuestionsList() != null
            ? exam.getQuestionsList().stream()
                .map(this::mapQuestionToQuestionDetailResponseDTO)
                .collect(Collectors.toList())
            : Collections.emptyList());
    return dto;
  }

  // Estos métodos de mapeo también podrían estar en QuestionService o una clase Mapper
  private QuestionDetailResponseDTO mapQuestionToQuestionDetailResponseDTO(Question question) {
    QuestionDetailResponseDTO dto = new QuestionDetailResponseDTO();
    dto.setId(question.getId());
    dto.setText(question.getText());
    if (question.getExam() != null) {
      dto.setExamId(question.getExam().getId());
    }
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
