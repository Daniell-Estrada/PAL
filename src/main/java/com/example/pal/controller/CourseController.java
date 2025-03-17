package com.example.pal.controller;

import com.example.pal.dto.course.CourseDTO;
import com.example.pal.dto.course.CreateCourseDTO;
import com.example.pal.dto.course.UpdateCourseDTO;
import com.example.pal.service.CourseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
  @Autowired private CourseService courseService;

  @PostMapping("/create")
  public ResponseEntity<CourseDTO> createCourse(@ModelAttribute CreateCourseDTO courseDTO) {
    CourseDTO course = courseService.createCourse(courseDTO);
    return ResponseEntity.status(201).body(course);
  }

  @GetMapping("/all")
  public ResponseEntity<List<CourseDTO>> getAllCourses() {
    return ResponseEntity.ok(courseService.getAllCourses());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
    return ResponseEntity.ok(courseService.getCourseById(id));
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<CourseDTO> updateCourse(
      @PathVariable Long id, @RequestBody UpdateCourseDTO courseDetails) {
    return ResponseEntity.ok(courseService.updateCourse(id, courseDetails));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
    courseService.deleteCourse(id);
    return ResponseEntity.noContent().build();
  }
}
