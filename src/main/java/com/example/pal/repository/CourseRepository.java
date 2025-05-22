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

    List<Course> findByInstructorId(Long instructorId);

    boolean existsByTitle(String title);

    @Query("SELECT c FROM Course c WHERE c.price = :price")
    List<Course> findByPrice(@Param("price") double price);

    @Query("SELECT c FROM Course c WHERE LOWER(c.category.name) = LOWER(:categoryName)")
    List<Course> findByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT c FROM Course c " +
            "WHERE " +
            "(:keyword IS NULL OR " +
            "LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.category.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:free IS NULL OR c.free = :free) " +
            "AND (:difficulty IS NULL OR LOWER(c.difficulty) = LOWER(:difficulty))")
    List<Course> searchCourses(
            @Param("keyword") String keyword,
            @Param("free") Boolean free,
            @Param("difficulty") String difficulty);
            

    @Query("SELECT c FROM Course c " +
            "WHERE " +
            "(:keyword IS NULL OR " +
            "LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.category.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:free IS NULL OR c.free = :free) " +
            "AND (:difficulty IS NULL OR LOWER(c.difficulty) = LOWER(:difficulty)) " +
            "AND (:categoryId IS NULL OR c.category.id = :categoryId)")
    List<Course> searchCoursesByCategory(
            @Param("keyword") String keyword,
            @Param("free") Boolean free,
            @Param("difficulty") String difficulty,
            @Param("categoryId") Long categoryId);

}
