package com.example.pal.model;

import jakarta.persistence.*;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "exams")
public class Exam {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title", nullable = false)
  private String title;

  @ElementCollection
  @CollectionTable(name = "exam_questions", joinColumns = @JoinColumn(name = "exam_id"))
  @Column(name = "question", nullable = false)
  private Set<String> questions;

  @ElementCollection
  @CollectionTable(name = "exam_answers", joinColumns = @JoinColumn(name = "exam_id"))
  @Column(name = "answer", nullable = false)
  private Set<String> answers;

  @ManyToOne
  @JoinColumn(name = "course_id")
  private Course course;

  @OneToMany(mappedBy = "exam")
  private Set<Score> scores;
}
