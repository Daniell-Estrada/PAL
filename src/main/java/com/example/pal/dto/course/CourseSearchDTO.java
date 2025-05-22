package com.example.pal.dto.course;

import lombok.Data;

@Data
public class CourseSearchDTO {
    private String keyword;      
    private Boolean free;        
    private String difficulty;   
    private String sortBy;   
    private Long categoryId; 
}