package com.example.pal.config;

import com.example.pal.dto.score.ExamResultDTO;
import com.example.pal.dto.user.StudentProgressDTO;
import com.example.pal.model.Enrollment;
import com.example.pal.model.Score;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();

    // Configuración personalizada para Score -> ExamResultDTO
    modelMapper
        .typeMap(Score.class, ExamResultDTO.class)
        .addMappings(
            mapper -> {
              mapper.map(src -> src.getExam().getTitle(), ExamResultDTO::setExamTitle);
              mapper.map(src -> src.getIssueDate(), ExamResultDTO::setExamDate);
              mapper.map(Score::getScore, ExamResultDTO::setScore);
            });

    // Configuración para Enrollment -> StudentProgressDTO
    modelMapper
        .typeMap(Enrollment.class, StudentProgressDTO.class)
        .addMappings(
            mapper ->
                mapper.map(
                    src -> src.getStudent().getUsername(), StudentProgressDTO::setStudentName));

    modelMapper
        .getConfiguration()
        .setMatchingStrategy(MatchingStrategies.STRICT)
        .setSkipNullEnabled(true);

    return modelMapper;
  }
}
