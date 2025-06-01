package com.example.pal.dto.exam;

import lombok.Data;

@Data
public class OptionDetailResponseDTO {
    private Long id;
    private String text;
    private boolean isCorrect;
    private Long questionId;
}
