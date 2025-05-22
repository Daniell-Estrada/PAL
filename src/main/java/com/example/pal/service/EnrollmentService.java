package com.example.pal.service;

import com.example.pal.dto.enrollment.EnrolledCourseDTO;
import com.example.pal.dto.enrollment.EnrollmentDTO;
import com.example.pal.dto.enrollment.RegisterEnrollmentDTO;
import com.example.pal.model.Course;
import com.example.pal.model.Enrollment;
import com.example.pal.model.User;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.EnrollmentRepository;
import com.example.pal.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {

  @Autowired private EnrollmentRepository enrollmentRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private CourseRepository courseRepository;

  @Autowired private ModelMapper modelMapper;

  public EnrollmentDTO registerEnrollment(RegisterEnrollmentDTO registerEnrollmentDTO) {
    // Verificar si el estudiante ya está inscrito en el curso
    if (enrollmentRepository.existsByStudentIdAndCourseId(
        registerEnrollmentDTO.getStudentId(), registerEnrollmentDTO.getCourseId())) {
      throw new IllegalStateException("El estudiante ya está inscrito en este curso");
    }

    // Obtener el estudiante y el curso
    User student =
        userRepository
            .findById(registerEnrollmentDTO.getStudentId())
            .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado"));

    Course course =
        courseRepository
            .findById(registerEnrollmentDTO.getCourseId())
            .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

    // Verificar pago si el curso tiene costo
    if (course.getPrice() > 0 && !registerEnrollmentDTO.isPaid()) {
      throw new IllegalStateException("El pago es requerido para inscribirse en este curso");
    }

    // Crear la inscripción
    Enrollment enrollment = new Enrollment();
    enrollment.setStudent(student);
    enrollment.setCourse(course);
    enrollment.setEnrollmentDate(LocalDateTime.now());
    enrollment.setPercentage(0.0); // Iniciar con 0% de progreso
    enrollment.setStatus("en progreso");

    // Guardar y devolver
    Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
    return modelMapper.map(savedEnrollment, EnrollmentDTO.class);
  }

  public List<EnrollmentDTO> getAllEnrollments() {
    return enrollmentRepository.findAll().stream()
        .map(enrollment -> modelMapper.map(enrollment, EnrollmentDTO.class))
        .collect(Collectors.toList());
  }

  public EnrollmentDTO getEnrollmentById(Long id) {
    Enrollment enrollment =
        enrollmentRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Inscripción no encontrada"));
    return modelMapper.map(enrollment, EnrollmentDTO.class);
  }

  public List<EnrolledCourseDTO> getEnrollmentsByStudentId(Long studentId) {
    return enrollmentRepository.findByStudentId(studentId).stream()
        .map(
            enrollment -> {
              EnrolledCourseDTO dto = new EnrolledCourseDTO();

              // Información básica de la inscripción
              dto.setId(enrollment.getId());
              dto.setTitle(enrollment.getCourse().getTitle());
              dto.setDescription(enrollment.getCourse().getDescription());
              dto.setEnrollmentDate(enrollment.getEnrollmentDate());

              // Mapeo del estado según el formato requerido
              String status = enrollment.getStatus().toLowerCase();
              dto.setStatus(status);

              dto.setProgress(enrollment.getPercentage());

              dto.setInstructorName(enrollment.getCourse().getInstructor().getUsername());
              dto.setCategoryName(enrollment.getCourse().getCategory().getName());

              return dto;
            })
        .collect(Collectors.toList());
  }

  public List<EnrollmentDTO> getEnrollmentsByCourseId(Long courseId) {
    return enrollmentRepository.findByCourseId(courseId).stream()
        .map(enrollment -> modelMapper.map(enrollment, EnrollmentDTO.class))
        .collect(Collectors.toList());
  }

  public List<EnrollmentDTO> getEnrollmentsByUsername(String username) {
    User student = userRepository.findByUsername(username);
    if (student == null) {
      throw new EntityNotFoundException("Usuario no encontrado");
    }

    return enrollmentRepository.findByStudentId(student.getId()).stream()
        .map(
            enrollment -> {
              EnrollmentDTO dto = modelMapper.map(enrollment, EnrollmentDTO.class);
              // Añadir información adicional del curso
              dto.setCourseTitle(enrollment.getCourse().getTitle());
              dto.setEnrollmentDate(enrollment.getEnrollmentDate());
              dto.setStatus(enrollment.getStatus());
              return dto;
            })
        .collect(Collectors.toList());
  }

  public EnrollmentDTO updateProgress(Long enrollmentId, Double percentage) {
    if (percentage < 0.0 || percentage > 1.0) {
      throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100");
    }

    Enrollment enrollment =
        enrollmentRepository
            .findById(enrollmentId)
            .orElseThrow(() -> new EntityNotFoundException("Inscripción no encontrada"));

    enrollment.setPercentage(percentage);

    // Actualizar estado si es necesario
    if (percentage == 1.0) {
      enrollment.setStatus("completado");
    } else {
      enrollment.setStatus("en progreso");
    }

    Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
    return modelMapper.map(updatedEnrollment, EnrollmentDTO.class);
  }

  public EnrollmentDTO markAsCompleted(Long enrollmentId) {
    Enrollment enrollment =
        enrollmentRepository
            .findById(enrollmentId)
            .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));
    enrollment.setStatus("completado");
    enrollmentRepository.save(enrollment);
    return modelMapper.map(enrollment, EnrollmentDTO.class);
  }
}
