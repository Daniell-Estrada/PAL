package com.example.pal.dto.exam;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class ExamSubmissionDTO {
    @NotNull
    private Long studentId;

    @NotEmpty
    private List<StudentAnswerSubmissionDTO> answers;
}
