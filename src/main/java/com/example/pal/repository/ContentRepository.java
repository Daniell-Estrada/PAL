package com.example.pal.repository;

import com.example.pal.model.Content;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
  List<Content> findByCourseId(Long courseId);
}
