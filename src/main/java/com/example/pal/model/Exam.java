package com.example.pal.model;

import jakarta.persistence.*;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "exams")
@EqualsAndHashCode(
    exclude = {
      "questionsList",
      "scores",
      "course",
      "studentExamAttempts"
    }) // studentExamAttempts también si existe y es bidireccional
@ToString(exclude = {"questionsList", "scores", "course", "studentExamAttempts"})
public class Exam {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title", nullable = false)
  private String title;

  @OneToMany(
      mappedBy = "exam",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private Set<Question> questionsList;

  @ElementCollection
  @CollectionTable(name = "exam_answers", joinColumns = @JoinColumn(name = "exam_id"))
  @Column(name = "answer", nullable = false)
  private Set<String> answers;

  @ManyToOne
  @JoinColumn(name = "course_id")
  private Course course;

  @OneToMany(mappedBy = "exam")
  private Set<Score> scores;

  @OneToMany(mappedBy = "exam")
  private Set<StudentExamAttempt> studentExamAttempts;
}
