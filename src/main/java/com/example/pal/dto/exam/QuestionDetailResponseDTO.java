package com.example.pal.dto.exam;

import lombok.Data;
import java.util.List;

@Data
public class QuestionDetailResponseDTO {
    private Long id;
    private String text;
    private Long examId;
    private List<OptionDetailResponseDTO> options; // Lista de opciones (puede estar vacía inicialmente)
}
