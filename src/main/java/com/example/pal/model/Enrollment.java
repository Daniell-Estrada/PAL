package com.example.pal.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "enrollments")
public class Enrollment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "percentage", nullable = false)
  private Double percentage;

  @Column(name = "enrollment_date", nullable = false)
  private String enrollmentDate;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User student;

  @ManyToOne
  @JoinColumn(name = "course_id")
  private Course course;
}
