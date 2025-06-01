package com.example.pal.dto.exam;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOptionRequestDTO {
    @NotBlank
    private String text; // Texto de la opción

    @NotNull
    private Boolean isCorrect; // true si esta es la opción correcta
}
