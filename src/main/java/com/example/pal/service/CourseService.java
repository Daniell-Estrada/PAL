package com.example.pal.service;

import com.example.pal.dto.course.CourseDTO;
import com.example.pal.dto.course.CreateCourseDTO;
import com.example.pal.model.Category;
import com.example.pal.model.Course;
import com.example.pal.model.User;
import com.example.pal.repository.CategoryRepository;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
  @Autowired private CourseRepository courseRepository;
  @Autowired private ModelMapper modelMapper;
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private UserRepository userRepository;

  public CourseDTO createCourse(CreateCourseDTO courseDTO) {
    Category category = categoryRepository.findByName(courseDTO.getCategoryName()).orElse(null);
    User user = userRepository.findById(courseDTO.getUserId()).orElse(null);

    if (category == null || user == null) {
      return null;
    }

    Course course = modelMapper.map(courseDTO, Course.class);
    Course savedCourse = courseRepository.save(course);
    return modelMapper.map(savedCourse, CourseDTO.class);
  }
}
