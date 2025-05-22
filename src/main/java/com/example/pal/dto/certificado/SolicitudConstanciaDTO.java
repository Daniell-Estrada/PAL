package com.example.pal.dto.certificado;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SolicitudConstanciaDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La matrícula es obligatoria")
    @Size(min = 6, max = 12, message = "La matrícula debe tener entre 6 y 12 caracteres")
    private String matricula;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Correo electrónico inválido")
    private String correo;

    @NotBlank(message = "El tipo de constancia es obligatorio")
    private String tipoConstancia;
}