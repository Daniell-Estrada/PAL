package com.example.pal.repository;

import com.example.pal.model.Course;
import com.example.pal.model.Enrollment;
import com.example.pal.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
  List<Enrollment> findByCourseId(Long courseId);

  Optional<Enrollment> findByStudentAndCourse(User student, Course course);

  boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

  List<Enrollment> findByStudentId(Long studentId);
}

