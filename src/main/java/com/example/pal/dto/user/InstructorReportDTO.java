package com.example.pal.dto.user;

import lombok.Data;

@Data
public class InstructorReportDTO {
  private Integer totalCourses;
  private Integer totalStudents;
  private Double completionRate;
  private Double averageScore;
}
