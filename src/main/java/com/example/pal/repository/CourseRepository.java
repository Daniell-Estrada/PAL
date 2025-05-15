package com.example.pal.repository;

import com.example.pal.model.Course;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
  Course findByTitle(String title);

  @Query("SELECT c FROM Course c WHERE c.price = (:price)")
  List<Course> findByPrice(@Param("price") double price);

  @Query("SELECT c FROM Course c WHERE LOWER(c.category.name) = LOWER(:categoryName)")
  List<Course> findByCategoryName(@Param("categoryName") String categoryName);

}
