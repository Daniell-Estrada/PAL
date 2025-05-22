package com.example.pal.repository;

import com.example.pal.model.Enrollment;
import com.example.pal.model.User;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
  List<Enrollment> findByCourseId(Long courseId);

  boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

  List<Enrollment> findByStudentId(Long studentId);
}