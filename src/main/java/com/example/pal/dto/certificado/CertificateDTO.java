package com.example.pal.dto.certificado;

import java.time.LocalDate;
import lombok.Data;

@Data
public class CertificateDTO {
  private Long id;
  private String studentName;
  private String courseTitle;
  private LocalDate issueDate;
  private Long cursoId;
}

