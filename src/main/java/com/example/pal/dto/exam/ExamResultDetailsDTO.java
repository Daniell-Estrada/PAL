package com.example.pal.dto.exam;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExamResultDetailsDTO {
    private Long attemptId;
    private Long examId;
    private String examTitle;
    private Long studentId;
    private String studentUsername;
    private Double score;
    private LocalDateTime submissionDate;
    private int totalQuestions;
    private int correctAnswersCount;
    private List<AnswerDetailDTO> answers;
    private String generalComments; // Placeholder for general comments
}
