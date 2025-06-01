package com.example.pal.dto.exam;

import lombok.Data;
import java.util.List;

@Data
public class AnswerDetailDTO {
    private Long questionId;
    private String questionText;
    private Long selectedOptionId;
    private String selectedOptionText;
    private Boolean wasCorrect;
    private List<OptionDetailDTO> options; // All options for this question
}
