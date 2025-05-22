package com.example.pal.repository;

import com.example.pal.model.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {
  Long countByForumCourseIdAndUserId(Long courseId, Long userId);
}
