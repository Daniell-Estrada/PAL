package com.example.pal.repository;

import com.example.pal.model.Option;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
  List<Option> findByQuestionId(Long questionId);
}
