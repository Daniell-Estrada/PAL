package com.example.pal.service;

import com.example.pal.dto.course.CourseDTO;
import com.example.pal.dto.course.CreateCourseDTO;
import com.example.pal.dto.course.UpdateCourseDTO;
import com.example.pal.model.Category;
import com.example.pal.model.Course;
import com.example.pal.model.User;
import com.example.pal.repository.CategoryRepository;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
  @Autowired private CourseRepository courseRepository;
  @Autowired private ModelMapper modelMapper;
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private UserRepository userRepository;

  public CourseDTO createCourse(CreateCourseDTO courseDTO) {
    Category category = findCategoryById(courseDTO.getCategoryId());
    User instructor = findUserById(courseDTO.getInstructorId());

    configureModelMapper(CreateCourseDTO.class, Course.class);

    Course course = modelMapper.map(courseDTO, Course.class);
    course.setCategory(category);
    course.setInstructor(instructor);

    Course savedCourse = courseRepository.save(course);
    return modelMapper.map(savedCourse, CourseDTO.class);
  }

  public List<CourseDTO> getAllCourses() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    System.out.println("Username: " + username);
    return courseRepository.findAll().stream()
        .map(course -> modelMapper.map(course, CourseDTO.class))
        .toList();
  }

  public CourseDTO getCourseById(Long id) {
    Course course = findCourseById(id);
    return modelMapper.map(course, CourseDTO.class);
  }

  public CourseDTO updateCourse(Long id, UpdateCourseDTO courseDetails) {
    findCourseById(id);

    findCategoryById(courseDetails.getCategoryId());
    findUserById(courseDetails.getInstructorId());

    configureModelMapper(UpdateCourseDTO.class, Course.class);

    Course course = modelMapper.map(courseDetails, Course.class);
    course.setId(id);

    Course updatedCourse = courseRepository.save(course);
    return modelMapper.map(updatedCourse, CourseDTO.class);
  }

  public void deleteCourse(Long id) {
    if (!courseRepository.existsById(id)) {
      throw new EntityNotFoundException("Course not found with id: " + id);
    }
    courseRepository.deleteById(id);
  }

  private Category findCategoryById(Long categoryId) {
    return categoryRepository
        .findById(categoryId)
        .orElseThrow(
            () -> new EntityNotFoundException("Category not found with id: " + categoryId));
  }

  private User findUserById(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
  }

  private Course findCourseById(Long courseId) {
    return courseRepository
        .findById(courseId)
        .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));
  }

  private void configureModelMapper(Class<?> sourceClass, Class<Course> destinationClass) {
    modelMapper.getConfiguration().setAmbiguityIgnored(true);

    modelMapper
        .typeMap(sourceClass, destinationClass)
        .addMappings(mapper -> mapper.skip(Course::setId));
  }
}
