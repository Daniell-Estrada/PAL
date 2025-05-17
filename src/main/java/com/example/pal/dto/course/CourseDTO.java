package com.example.pal.dto.course;

import lombok.Data;

@Data
public class CourseDTO {
  private Long id;
  private String title;
  private String description;
  private double price;
  private Long categoryId;
  private Long instructorId;
  private String difficulty;
  private boolean free;
}
