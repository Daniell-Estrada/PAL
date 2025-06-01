package com.example.pal.dto.exam;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentAnswerSubmissionDTO {
    @NotNull
    private Long questionId;

    @NotNull
    private Long selectedOptionId;
}
