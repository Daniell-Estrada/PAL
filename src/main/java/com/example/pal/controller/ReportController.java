package com.example.pal.controller;

import com.example.pal.dto.report.ProgressReportDTO;
import com.example.pal.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
  @Autowired private ReportService reportService;

  @GetMapping("/progress/{courseId}")
  public ResponseEntity<ProgressReportDTO> getProgressReport(@PathVariable Long courseId) {
    return ResponseEntity.ok(reportService.generateProgressReport(courseId));
  }
}
