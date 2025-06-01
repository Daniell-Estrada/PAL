package com.example.pal.dto.exam;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateQuestionRequestDTO {
    @NotBlank
    private String text; // Texto de la pregunta
}
