package com.example.pal.repository;

import com.example.pal.model.Enrollment;
import com.example.pal.model.Course;
import com.example.pal.model.User;

import java.util.List;
// import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByStudentAndCourse(User student, Course course);

    // Optional<Enrollment> findByUserAndCourse(User student, Course course);

    List<Enrollment> findByStudent(User student);
}