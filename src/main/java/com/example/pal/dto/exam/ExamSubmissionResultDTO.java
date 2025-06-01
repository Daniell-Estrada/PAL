package com.example.pal.dto.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamSubmissionResultDTO {
    private Long attemptId;
    private Double score;
    private int totalQuestions;
    private int correctAnswers;
    private String message;
}
