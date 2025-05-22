package com.example.pal.dto.enrollment;

import com.example.pal.dto.course.CourseDTO;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EnrollmentDTO {
  private Long id;
  private Long studentId;
  private String studentName;
  private Long courseId;
  private String courseTitle;
  private LocalDateTime enrollmentDate;
  private boolean paid;
  private int percentage;
  private String status; // "Completado", "En progreso", "No iniciado"
  private CourseDTO course; // Agregado para la relación con Course
}
