package com.example.pal.controller;

import com.example.pal.dto.content.ContentDTO;
import com.example.pal.dto.file.FileDTO;
import com.example.pal.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    @Autowired private ContentService contentService;

    @PostMapping("/create/{courseId}")
    public ResponseEntity<ContentDTO> createContent(
            @PathVariable Long courseId, @RequestParam("type") String type) {
        ContentDTO content = contentService.createContent(courseId, type);
        return ResponseEntity.ok(content);
    }

    @PostMapping("/upload/{contentId}")
    public ResponseEntity<FileDTO> uploadFile(
            @PathVariable Long contentId, @RequestParam("file") MultipartFile file) {
        try {
            FileDTO uploadedFile = contentService.uploadFile(contentId, file);
            return ResponseEntity.ok(uploadedFile);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

