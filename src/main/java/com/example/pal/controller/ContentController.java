package com.example.pal.controller;

import com.example.pal.dto.content.ContentDTO;
import com.example.pal.dto.content.CreateContentDTO;
import com.example.pal.service.ContentService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    @PostMapping("/upload")
    public ResponseEntity<ContentDTO> uploadContent(
            @RequestParam("file") MultipartFile file,
            @RequestParam("courseId") Long courseId) {
    
        CreateContentDTO dto = new CreateContentDTO();
        dto.setFile(file);
        dto.setCourseId(courseId);
    
        ContentDTO contentDTO = contentService.uploadContent(dto);
        return ResponseEntity.ok(contentDTO);
    }
    


    @GetMapping("/all")
    public ResponseEntity<List<ContentDTO>> getAllContent() {
        return ResponseEntity.ok(contentService.getAllContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentDTO> getContentById(@PathVariable Long id) {
        Optional<ContentDTO> content = contentService.getContentById(id);
        return content.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
public ResponseEntity<ContentDTO> updateContent(
        @PathVariable Long id,
        @RequestParam(value = "file", required = false) MultipartFile file,
        @RequestParam("type") String type) {

    return ResponseEntity.ok(contentService.updateContent(id, file, type));
}

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }
}
  

