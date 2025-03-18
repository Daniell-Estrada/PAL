package com.example.pal.dto.content;

import lombok.Data;
import java.util.List;
import com.example.pal.dto.file.FileDTO;

@Data
public class ContentDTO {
    private Long id;
    private String type;
    private Long courseId;
    private List<FileDTO> files;
}
