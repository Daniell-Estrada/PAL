package com.example.pal.dto.enrollment;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EnrolledCourseDTO {
  private Long id;
  private String title;
  private String description;
  private LocalDateTime enrollmentDate;
  private String status; // "completado", "in_progress", "not_started"
  private Double progress;
  private String instructorName;
  private String categoryName;
}
