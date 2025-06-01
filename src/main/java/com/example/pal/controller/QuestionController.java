package com.example.pal.controller;

import com.example.pal.dto.exam.CreateOptionRequestDTO;
import com.example.pal.dto.exam.OptionDetailResponseDTO;
import com.example.pal.service.QuestionService; // O un OptionService dedicado
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

  private final QuestionService questionService; // O un OptionService

  @PostMapping("/{questionId}/options")
  public ResponseEntity<OptionDetailResponseDTO> addOptionToQuestion(
      @PathVariable Long questionId,
      @Valid @RequestBody CreateOptionRequestDTO createOptionRequestDTO) {
    OptionDetailResponseDTO createdOption =
        questionService.addOptionToQuestion(questionId, createOptionRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdOption);
  }
}
