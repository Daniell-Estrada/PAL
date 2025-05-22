package com.example.pal.service;

import com.example.pal.dto.enrollment.EnrollmentDTO;
import com.example.pal.dto.enrollment.RegisterEnrollmentDTO;
import com.example.pal.model.Course;
import com.example.pal.model.Enrollment;
import com.example.pal.model.User;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.EnrollmentRepository;
import com.example.pal.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    public EnrollmentDTO registerEnrollment(RegisterEnrollmentDTO dto) {
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("El estudiante no existe"));
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("El curso no existe"));

        // Validar que no esté ya inscrito
        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en este curso");
        }

        // Validar pago si el curso no es gratis
        if (!course.isFree() && !dto.isPaid()) {
            throw new IllegalArgumentException("Debe realizar el pago para inscribirse en este curso");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setPaid(dto.isPaid() || course.isFree());

        Enrollment saved = enrollmentRepository.save(enrollment);
        return modelMapper.map(saved, EnrollmentDTO.class);
    }

    public List<EnrollmentDTO> getEnrollmentsByStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("El estudiante no existe"));
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        return enrollments.stream()
                .map(enrollment -> modelMapper.map(enrollment, EnrollmentDTO.class))
                .collect(Collectors.toList());
    }

    public EnrollmentDTO togglePaid(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("La inscripción no existe"));
        enrollment.setPaid(!enrollment.isPaid());
        Enrollment saved = enrollmentRepository.save(enrollment);
        return modelMapper.map(saved, EnrollmentDTO.class);
    }

    public EnrollmentDTO markAsCompleted(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));
        enrollment.setStatus("completado");
        enrollmentRepository.save(enrollment);
        return modelMapper.map(enrollment, EnrollmentDTO.class);
    }
}