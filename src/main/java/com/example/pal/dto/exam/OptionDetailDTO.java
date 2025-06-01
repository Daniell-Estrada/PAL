package com.example.pal.dto.exam;

import lombok.Data;

@Data
public class OptionDetailDTO {
    private Long id;
    private String text;
    private boolean isCorrect; // The actual correct status of this option
    private boolean isSelectedByStudent; // If this option was selected by the student
}
