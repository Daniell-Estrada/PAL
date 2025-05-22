package com.example.pal.repository;

import com.example.pal.model.Score;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
  List<Score> findByExamCourseIdAndUserId(Long courseId, Long userId);
}
