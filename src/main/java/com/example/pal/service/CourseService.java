package com.example.pal.service;

import com.example.pal.dto.course.CourseDTO;
import com.example.pal.dto.course.CourseSearchDTO;
import com.example.pal.dto.course.CreateCourseDTO;
import com.example.pal.dto.course.UpdateCourseDTO;
import com.example.pal.model.Category;
import com.example.pal.model.Course;
import com.example.pal.model.User;
import com.example.pal.repository.CategoryRepository;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private UserRepository userRepository;

  @PostConstruct
  public void setupMapper() {
    modelMapper.typeMap(Course.class, CourseDTO.class).addMappings(mapper -> {
      mapper.map(src -> src.getCategory().getId(), CourseDTO::setCategoryId);
      mapper.map(src -> src.getInstructor().getId(), CourseDTO::setInstructorId);
    });
  }

  public CourseDTO createCourse(CreateCourseDTO courseDTO) {
    if (courseRepository.existsByTitle(courseDTO.getTitle())) {
      throw new IllegalArgumentException("Ya existe un curso con ese título.");
    }

    Category category = categoryRepository.findById(courseDTO.getCategoryId())
        .orElseThrow(() -> new IllegalArgumentException("La categoría no existe"));

    User instructor = userRepository.findById(courseDTO.getInstructorId())
        .orElseThrow(() -> new IllegalArgumentException("El instructor no existe"));

    configureModelMapper(CreateCourseDTO.class, Course.class);

    Course course = modelMapper.map(courseDTO, Course.class);
    course.setCategory(category);
    course.setInstructor(instructor);
    course.setDifficulty(courseDTO.getDifficulty());
    course.setFree(courseDTO.getFree());

    Course savedCourse = courseRepository.save(course);
    return modelMapper.map(savedCourse, CourseDTO.class);
  }

  public List<CourseDTO> getAllCourses() {
    return courseRepository.findAll().stream()
        .map(course -> modelMapper.map(course, CourseDTO.class))
        .collect(Collectors.toList());
  }

  public CourseDTO getCourseById(Long id) {
    Course course = findCourseById(id);
    return modelMapper.map(course, CourseDTO.class);
  }

  public List<CourseDTO> getCoursesByInstructor(Long instructorId) {
    List<Course> courses = courseRepository.findByInstructorId(instructorId);
    return courses.stream()
        .map(course -> modelMapper.map(course, CourseDTO.class))
        .collect(Collectors.toList());
  }

  public CourseDTO updateCourse(Long id, UpdateCourseDTO courseDetails) {
    Course course = findCourseById(id);

    // Category category = findCategoryById(courseDetails.getCategoryId());
    // User instructor = findUserById(courseDetails.getInstructorId());

    configureModelMapper(UpdateCourseDTO.class, Course.class);

    course = modelMapper.map(courseDetails, Course.class);
    course.setId(id);

    Course updatedCourse = courseRepository.save(course);
    return modelMapper.map(updatedCourse, CourseDTO.class);
  }

  public CourseDTO partialUpdateCourse(Long id, UpdateCourseDTO courseDetails) {
    Course course = findCourseById(id);

    if (courseDetails.getTitle() != null) {
      course.setTitle(courseDetails.getTitle());
    }
    if (courseDetails.getDescription() != null) {
      course.setDescription(courseDetails.getDescription());
    }
    if (courseDetails.getPrice() != null) {
      course.setPrice(courseDetails.getPrice());
    }
    if (courseDetails.getCategoryId() != null) {
      Category category = findCategoryById(courseDetails.getCategoryId());
      course.setCategory(category);
    }
    if (courseDetails.getInstructorId() != null) {
      User instructor = findUserById(courseDetails.getInstructorId());
      course.setInstructor(instructor);
    }
    if (courseDetails.getDifficulty() != null) {
      course.setDifficulty(courseDetails.getDifficulty());
    }
    if (courseDetails.getFree() != null) {
      course.setFree(courseDetails.getFree());
    }

    Course updatedCourse = courseRepository.save(course);
    return modelMapper.map(updatedCourse, CourseDTO.class);
  }

  public void deleteCourse(Long id) {
    if (!courseRepository.existsById(id)) {
      throw new EntityNotFoundException("Course not found with id: " + id);
    }
    courseRepository.deleteById(id);
  }

  public List<CourseDTO> getFreeCourses() {
    List<Course> freeCourses = courseRepository.findByPrice(0.0);
    return freeCourses.stream()
        .map(course -> modelMapper.map(course, CourseDTO.class))
        .toList();
  }

  public List<CourseDTO> getCoursesByCategory(String categoryName) {
    List<Course> courses = courseRepository.findByCategoryName(categoryName);
    return courses.stream()
        .map(course -> modelMapper.map(course, CourseDTO.class))
        .toList();
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

  /**
   * 
   * @param sourceClass
   * @param destinationClass
   */
  private void configureModelMapper(Class<?> sourceClass, Class<Course> destinationClass) {
    modelMapper.getConfiguration().setAmbiguityIgnored(true);

    modelMapper
        .typeMap(sourceClass, destinationClass)
        .addMappings(mapper -> mapper.skip(Course::setId));
  }

  public List<CourseDTO> searchCourses(CourseSearchDTO dto) {
    List<Course> results = courseRepository.searchCourses(
        dto.getKeyword(),
        dto.getFree(),
        dto.getDifficulty());

    // Ordenamiento por fecha si se solicita
    if ("date".equalsIgnoreCase(dto.getSortBy())) {
      results.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt())); // más recientes primero
    }

    return results.stream()
        .map(course -> modelMapper.map(course, CourseDTO.class))
        .collect(Collectors.toList());
  }
}
