package com.example.pal.dto.content;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CreateContentDTO {
    private MultipartFile file;
    private Long courseId;
}
