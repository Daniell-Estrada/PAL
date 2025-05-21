package com.example.pal.dto.user;

import com.example.pal.dto.score.ExamResultDTO;
import java.util.Set;
import lombok.Data;

@Data
public class StudentProgressDTO {
  private String studentName;
  private Double courseProgress;
  private Double averageScore;
  private Integer forumMessages;
  private Set<ExamResultDTO> examResults;
}
