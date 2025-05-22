package com.example.pal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class SolicitudConstancia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String matricula;
    private String correo;
    private String tipoConstancia;
    private LocalDateTime fechaSolicitud;
    private String estado;
}