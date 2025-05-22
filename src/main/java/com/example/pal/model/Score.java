package com.example.pal.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(name = "scores")
public class Score {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "score", nullable = false)
  private Double score;

  @Column(name = "issueDate", nullable = false)
  private LocalDate issueDate;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "exam_id")
  private Exam exam;
}
