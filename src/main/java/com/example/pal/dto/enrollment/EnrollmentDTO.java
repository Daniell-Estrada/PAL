package com.example.pal.dto.enrollment;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EnrollmentDTO {
    private Long id;
    private Long studentId;
    private Long courseId;
    private boolean paid;
    private LocalDateTime enrollmentDate;
    private String status;
}