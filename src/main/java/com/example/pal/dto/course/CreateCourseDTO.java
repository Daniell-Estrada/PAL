package com.example.pal.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CreateCourseDTO {

  @NotBlank(message = "El título no puede estar vacío")
  //unico
  private String title;

  @NotBlank(message = "La descripción no puede estar vacía")
  private String description;

  @NotNull(message = "El curso debe tener precio")
  @PositiveOrZero(message = "El precio del curso no puede ser negativo")
  private Double price;

  @NotNull(message = "Debe seleccionar una categoría")
  private Long categoryId;

  @NotNull(message = "Debe seleccionar un instructor")
  private Long instructorId;

  @NotBlank(message = "La dificultad no puede estar vacía")
  private String difficulty;

  @NotNull(message = "Debe indicar si el curso es gratuito o no")
  private Boolean free;
}