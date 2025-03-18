package com.example.pal.dto.content;

import lombok.Data;

@Data
public class CreateContentDTO {
    private String type;
    private String url;
    private Long courseId;
}
