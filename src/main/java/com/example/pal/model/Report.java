package com.example.pal.model;

import jakarta.persistence.*;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "reports")
public class Report {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "data", columnDefinition = "TEXT", nullable = false)
  private String data;

  @ManyToOne
  @JoinColumn(name = "course_id", nullable = false)
  private Course course;

  @Transient private Set<Enrollment> enrollments;

  public Set<Enrollment> getEnrollments() {
    return this.course != null ? this.course.getEnrollments() : null;
  }

  @Transient private Set<Forum> forums;

  @Transient private Integer forumParticipationCount;
}
