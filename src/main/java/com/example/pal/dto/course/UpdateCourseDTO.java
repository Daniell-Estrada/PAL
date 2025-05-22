package com.example.pal.dto.course;

import lombok.Data;

@Data
public class UpdateCourseDTO {
  private String title;
  private String description;
  private Double price;
  private Long instructorId;
  private Long categoryId;
  private String difficulty;
  private Boolean free;
}
