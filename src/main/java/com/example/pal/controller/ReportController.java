package com.example.pal.controller;

import com.example.pal.dto.report.ProgressReportDTO;
import com.example.pal.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
  @Autowired private ReportService reportService;

  @GetMapping("/progress/{courseId}")
  public ResponseEntity<ProgressReportDTO> getProgressReport(@PathVariable Long courseId) {
    return ResponseEntity.ok(reportService.generateProgressReport(courseId));
  }

  @GetMapping("/export/{courseId}")
  public ResponseEntity<Resource> exportReport(
      @PathVariable Long courseId, @RequestParam(defaultValue = "csv") String format) {

    if ("pdf".equalsIgnoreCase(format)) {
      return reportService.exportReportPDF(courseId);
    } else {
      return reportService.exportReportCSV(courseId);
    }
  }
}
