package com.example.pal.dto.exam;

import lombok.Data;
import java.util.List;

@Data
public class ExamDetailResponseDTO {
    private Long id;
    private String title;
    private Long courseId;
    private String courseTitle; // Opcional, para más detalle
    private List<QuestionDetailResponseDTO> questions; // Lista de preguntas (puede estar vacía inicialmente)
}