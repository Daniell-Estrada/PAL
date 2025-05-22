package com.example.pal.dto.report;

import com.example.pal.dto.user.StudentProgressDTO;
import java.time.LocalDate;
import java.util.Set;
import lombok.Data;

@Data
public class ProgressReportDTO {
  private String courseTitle;
  private LocalDate generatedDate;
  private Set<StudentProgressDTO> students;
}
