package com.example.pal.controller;

import com.example.pal.dto.enrollment.EnrollmentDTO;
import com.example.pal.dto.enrollment.RegisterEnrollmentDTO;
import com.example.pal.service.EnrollmentService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/register")
    public ResponseEntity<EnrollmentDTO> register(@Valid @RequestBody RegisterEnrollmentDTO dto) {
        EnrollmentDTO enrollment = enrollmentService.registerEnrollment(dto);
        return ResponseEntity.status(201).body(enrollment);
    }

    @GetMapping("/my-courses")
    public ResponseEntity<List<EnrollmentDTO>> getMyCourses(@RequestParam Long studentId) {
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        return ResponseEntity.ok(enrollments);
    }

    @PatchMapping("/{enrollmentId}/toggle-paid")
    public ResponseEntity<EnrollmentDTO> togglePaid(@PathVariable Long enrollmentId) {
        EnrollmentDTO enrollment = enrollmentService.togglePaid(enrollmentId);
        return ResponseEntity.ok(enrollment);
    }

    @PatchMapping("/{enrollmentId}/complete")
    public ResponseEntity<EnrollmentDTO> markAsCompleted(@PathVariable Long enrollmentId) {
        EnrollmentDTO enrollmentDTO = enrollmentService.markAsCompleted(enrollmentId);
        return ResponseEntity.ok(enrollmentDTO);
    }
}