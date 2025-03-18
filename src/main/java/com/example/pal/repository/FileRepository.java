package com.example.pal.repository;

import com.example.pal.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByContentId(Long contentId);
}

