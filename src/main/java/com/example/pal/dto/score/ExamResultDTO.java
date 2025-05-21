package com.example.pal.dto.score;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ExamResultDTO {
  private String examTitle;
  private LocalDate examDate;
  private Double score;
}
