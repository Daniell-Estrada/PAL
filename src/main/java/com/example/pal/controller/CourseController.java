package com.example.pal.controller;

import com.example.pal.dto.course.CourseDTO;
import com.example.pal.dto.course.CourseSearchDTO;
import com.example.pal.dto.course.CreateCourseDTO;
import com.example.pal.dto.course.UpdateCourseDTO;
import com.example.pal.service.CourseService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
  @Autowired private CourseService courseService;

  @PostMapping("/create")
  public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CreateCourseDTO courseDTO) {
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

  @GetMapping("/by-instructor/{instructorId}")
  public ResponseEntity<List<CourseDTO>> getCoursesByInstructor(@PathVariable Long instructorId) {
    List<CourseDTO> courses = courseService.getCoursesByInstructor(instructorId);
    if (courses.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(courses);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<CourseDTO> updateCourse(
      @PathVariable Long id, @RequestBody UpdateCourseDTO courseDetails) {
    return ResponseEntity.ok(courseService.updateCourse(id, courseDetails));
  }

  @PatchMapping("/update/{id}")
  public ResponseEntity<CourseDTO> partialUpdateCourse(
      @PathVariable Long id, @RequestBody UpdateCourseDTO courseDetails) {
    return ResponseEntity.ok(courseService.partialUpdateCourse(id, courseDetails));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
    courseService.deleteCourse(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/free")
  public ResponseEntity<List<CourseDTO>> getFreeCourses() {
    List<CourseDTO> freeCourses = courseService.getFreeCourses();
    if (freeCourses.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(freeCourses);
  }

  @GetMapping("/by-category/{categoryName}")
  public ResponseEntity<List<CourseDTO>> getCoursesByCategory(@PathVariable String categoryName) {
    List<CourseDTO> courses = courseService.getCoursesByCategory(categoryName);
    if (courses.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(courses);
  }

  @GetMapping("/search")
  public List<CourseDTO> searchCourses(
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) Boolean free,
      @RequestParam(required = false) String difficulty,
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false) Long categoryId // Nuevo parámetro para filtrar por categoría
      ) {
    CourseSearchDTO dto = new CourseSearchDTO();
    dto.setKeyword(keyword);
    dto.setFree(free);
    dto.setDifficulty(difficulty);
    dto.setSortBy(sortBy);
    dto.setCategoryId(categoryId);

    return courseService.searchCourses(dto);
  }
}
