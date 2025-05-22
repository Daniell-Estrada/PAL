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
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

  public ResponseEntity<Resource> exportReportCSV(Long courseId) {
    ProgressReportDTO report = generateProgressReport(courseId);

    // Generar CSV
    StringBuilder csvContent = new StringBuilder();
    csvContent.append("Curso,Fecha de Generación\n");
    csvContent
        .append(report.getCourseTitle())
        .append(",")
        .append(report.getGeneratedDate())
        .append("\n\n");

    csvContent.append("Estudiante,Email,Progreso,Promedio,Mensajes en Foro\n");

    for (StudentProgressDTO student : report.getStudents()) {
      csvContent.append(student.getUsername()).append(",");
      csvContent.append(student.getEmail()).append(",");
      csvContent.append(student.getCourseProgress()).append("%,");
      csvContent.append(student.getAverageScore()).append(",");
      csvContent.append(student.getForumMessages()).append("\n");
    }

    byte[] bytes = csvContent.toString().getBytes(StandardCharsets.UTF_8);
    ByteArrayResource resource = new ByteArrayResource(bytes);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_" + courseId + ".csv")
        .contentType(MediaType.parseMediaType("text/csv"))
        .contentLength(bytes.length)
        .body(resource);
  }

  public ResponseEntity<Resource> exportReportPDF(Long courseId) {
    ProgressReportDTO report = generateProgressReport(courseId);

    // En una implementación real, aquí generaríamos un PDF
    // Por simplicidad, devolvemos un PDF vacío
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_" + courseId + ".pdf")
        .contentType(MediaType.APPLICATION_PDF)
        .body(resource);
  }
}
