package com.example.pal.repository;

import com.example.pal.model.StudentAnswer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
  List<StudentAnswer> findByStudentExamAttemptId(Long attemptId);
}
