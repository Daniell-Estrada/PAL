package com.example.pal.service;

import com.example.pal.dto.content.ContentDTO;
import com.example.pal.dto.file.FileDTO;
import com.example.pal.model.Content;
import com.example.pal.model.Course;
import com.example.pal.model.File;
import com.example.pal.repository.ContentRepository;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

@Service
public class ContentService {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired private ContentRepository contentRepository;
    @Autowired private FileRepository fileRepository;
    @Autowired private CourseRepository courseRepository;

    public ContentDTO createContent(Long courseId, String type) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Content content = new Content();
        content.setType(type);
        content.setCourse(course);

        contentRepository.save(content);
        return convertToDTO(content);
    }

    public FileDTO uploadFile(Long contentId, MultipartFile file) throws Exception {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Contenido no encontrado"));

        Path filePath = Path.of(UPLOAD_DIR + file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        File fileEntity = new File();
        fileEntity.setFileUrl(filePath.toString());
        fileEntity.setContent(content);

        fileRepository.save(fileEntity);
        return new FileDTO(fileEntity.getId(), fileEntity.getFileUrl());
    }

    private ContentDTO convertToDTO(Content content) {
        ContentDTO dto = new ContentDTO();
        dto.setId(content.getId());
        dto.setType(content.getType());
        dto.setCourseId(content.getCourse().getId());
        dto.setFiles(content.getFiles().stream()
            .map(file -> new FileDTO(file.getId(), file.getFileUrl()))
            .collect(Collectors.toList()));
        return dto;
    }
}

