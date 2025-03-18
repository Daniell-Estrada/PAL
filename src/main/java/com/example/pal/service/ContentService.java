package com.example.pal.service;

import com.example.pal.dto.content.ContentDTO;
import com.example.pal.dto.content.CreateContentDTO;
import com.example.pal.model.Content;
import com.example.pal.model.Course;
import com.example.pal.repository.ContentRepository;
import com.example.pal.repository.CourseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final CourseRepository courseRepository;

    public ContentDTO uploadContent(CreateContentDTO dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
    
        MultipartFile file = dto.getFile();
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("El archivo no puede estar vacío");
        }
    
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = "uploads/" + fileName;
    
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }
    
        Content content = new Content();
        content.setCourse(course);
        content.setType(file.getContentType());
        content.setUrl(filePath);
    
        Content savedContent = contentRepository.save(content);
        return mapToDTO(savedContent);
    }
    
    public List<ContentDTO> getAllContent() {
        return contentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ContentDTO> getContentById(Long id) {
        return contentRepository.findById(id).map(this::mapToDTO);
    }

    public ContentDTO updateContent(Long id, CreateContentDTO dto) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contenido no encontrado"));
    
        MultipartFile file = dto.getFile();
        if (file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = "uploads/" + fileName;
    
            try {
                Path path = Paths.get(filePath);
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());
                content.setUrl(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Error al actualizar el archivo", e);
            }
        }
    
        content.setType(file != null ? file.getContentType() : content.getType());
        
        return mapToDTO(contentRepository.save(content));
    }

    public void deleteContent(Long id) {
        contentRepository.deleteById(id);
    }

    private ContentDTO mapToDTO(Content content) {
        ContentDTO dto = new ContentDTO();
        dto.setId(content.getId());
        dto.setCourseId(content.getCourse().getId());
        dto.setType(content.getType());
        dto.setUrl(content.getUrl());
        return dto;
    }
}


