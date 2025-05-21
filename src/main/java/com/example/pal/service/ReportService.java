package com.example.pal.service;

import com.example.pal.dto.report.ProgressReportDTO;
import com.example.pal.dto.score.ExamResultDTO;
import com.example.pal.dto.user.StudentProgressDTO;
import com.example.pal.model.Course;
import com.example.pal.model.Enrollment;
import com.example.pal.model.Score;
import com.example.pal.model.User;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.EnrollmentRepository;
import com.example.pal.repository.ForumPostRepository;
import com.example.pal.repository.ScoreRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
  @Autowired private EnrollmentRepository enrollmentRepository;
  @Autowired private ScoreRepository scoreRepository;
  @Autowired private ForumPostRepository forumPostRepository;
  @Autowired private CourseRepository courseRepository;

  @Autowired private ModelMapper modelMapper;

  public ProgressReportDTO generateProgressReport(Long courseId) {
    Course course =
        courseRepository
            .findById(courseId)
            .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

    Set<StudentProgressDTO> students = new HashSet<>();

    List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);

    ProgressReportDTO report = new ProgressReportDTO();
    report.setCourseTitle(course.getTitle());
    report.setGeneratedDate(LocalDate.now());

    enrollments.forEach(
        enrollment -> {
          User student = enrollment.getStudent();

          StudentProgressDTO dto = modelMapper.map(student, StudentProgressDTO.class);
          modelMapper.map(enrollment, dto);

          dto.setCourseProgress(enrollment.getPercentage());

          List<Score> scores =
              scoreRepository.findByExamCourseIdAndUserId(courseId, student.getId());
          Set<ExamResultDTO> examResults =
              scores.stream()
                  .map(score -> modelMapper.map(score, ExamResultDTO.class))
                  .collect(Collectors.toSet());
          dto.setExamResults(examResults);

          Double average = scores.stream().mapToDouble(Score::getScore).average().orElse(0.0);
          dto.setAverageScore(average);

          Long forumCount =
              forumPostRepository.countByForumCourseIdAndUserId(courseId, student.getId());
          dto.setForumMessages(forumCount.intValue());
          students.add(dto);
        });
    report.setStudents(students);
    return report;
  }
}
