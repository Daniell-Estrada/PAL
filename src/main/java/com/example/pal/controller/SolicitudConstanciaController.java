package com.example.pal.controller;

import com.example.pal.dto.solicitudConstancia.SolicitudConstanciaDTO;
import com.example.pal.model.SolicitudConstancia;
import com.example.pal.service.SolicitudConstanciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudConstanciaController {

    private final SolicitudConstanciaService service;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@Valid @RequestBody SolicitudConstanciaDTO dto) throws Exception {
        SolicitudConstancia solicitud = service.registrarSolicitud(dto);
        String mensaje = "Solicitud registrada exitosamente. Se ha enviado la constancia al correo: "
                + solicitud.getCorreo();
        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/todas")
    public ResponseEntity<List<SolicitudConstancia>> todas() {
        return ResponseEntity.ok(service.obtenerSolicitudes());
    }
}