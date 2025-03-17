package com.example.pal.dto.course;

import lombok.Data;

@Data
public class CourseDTO {
  private Long id;
  private String title;
  private String description;
  private double price;
  private Long instructorId;
  private Long categoryId;
}
