package com.example.pal.controller;

import com.example.pal.dto.enrollment.EnrollmentDTO;
import com.example.pal.dto.enrollment.RegisterEnrollmentDTO;
import com.example.pal.service.EnrollmentService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

  @Autowired private EnrollmentService enrollmentService;

  @PostMapping("/register")
  @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN')")
  public ResponseEntity<EnrollmentDTO> registerEnrollment(
      @Valid @RequestBody RegisterEnrollmentDTO enrollmentDTO) {
    return ResponseEntity.ok(enrollmentService.registerEnrollment(enrollmentDTO));
  }

  @GetMapping("/all")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
    return ResponseEntity.ok(enrollmentService.getAllEnrollments());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR') or hasRole('ESTUDIANTE')")
  public ResponseEntity<EnrollmentDTO> getEnrollmentById(@PathVariable Long id) {
    return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
  }

  @GetMapping("/student/{studentId}")
  @PreAuthorize(
      "hasRole('ADMIN') or hasRole('INSTRUCTOR') or #studentId == authentication.principal.id")
  public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByStudentId(
      @PathVariable Long studentId) {
    return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudentId(studentId));
  }

  @GetMapping("/course/{courseId}")
  @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
  public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByCourseId(@PathVariable Long courseId) {
    return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourseId(courseId));
  }

  @GetMapping("/my-courses")
  @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN')")
  public ResponseEntity<List<EnrollmentDTO>> getMyEnrollments() {
    // Obtener el usuario autenticado
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();

    // Obtener las inscripciones del estudiante actual
    return ResponseEntity.ok(enrollmentService.getEnrollmentsByUsername(username));
  }

  @PutMapping("/update-progress/{enrollmentId}")
  @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN')")
  public ResponseEntity<EnrollmentDTO> updateProgress(
      @PathVariable Long enrollmentId, @RequestParam Double percentage) {

    return ResponseEntity.ok(enrollmentService.updateProgress(enrollmentId, percentage));
  }

    @PatchMapping("/{enrollmentId}/complete")
    public ResponseEntity<EnrollmentDTO> markAsCompleted(@PathVariable Long enrollmentId) {
        EnrollmentDTO enrollmentDTO = enrollmentService.markAsCompleted(enrollmentId);
        return ResponseEntity.ok(enrollmentDTO);
    }
}
