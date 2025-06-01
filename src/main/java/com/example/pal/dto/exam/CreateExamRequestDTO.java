package com.example.pal.dto.exam;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateExamRequestDTO {
    @NotBlank
    private String title;

    @NotNull
    private Long courseId; // ID del curso al que pertenece el examen
}
