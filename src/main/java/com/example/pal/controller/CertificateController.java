package com.example.pal.controller;

import com.example.pal.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    /**
     * Endpoint para generar un certificado de curso para un estudiante.
     * Solo los estudiantes que hayan completado el curso pueden generar el
     * certificado.
     */
    @PostMapping("/generate/{courseId}")
    public ResponseEntity<String> generateCertificate(@RequestParam Long userId, @PathVariable Long courseId) {
        try {
            certificateService.generateCertificate(userId, courseId);
            return ResponseEntity.ok("Certificado generado y enviado por correo.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al generar el certificado: " + e.getMessage());
        }
    }

    /**
     * Endpoint para descargar un certificado en formato PDF.
     */
    @GetMapping("/download/{certificateId}")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable Long certificateId) throws IOException {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=certificado.pdf")
                .body(certificateService.getCertificatePdf(certificateId));
    }

    // obtener todos los certificados 
    @GetMapping("/all")
    public ResponseEntity<?> getAllCertificates() {
        return ResponseEntity.ok(certificateService.getAllCertificates());
    }

    // eliminar un certificado
    @DeleteMapping("/delete/{certificateId}")
    public ResponseEntity<String> deleteCertificate(@PathVariable Long certificateId) {
        try {
            certificateService.deleteCertificate(certificateId);
            return ResponseEntity.ok("Certificado eliminado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar el certificado: " + e.getMessage());
        }
    }


    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getCertificatesByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(certificateService.getCertificatesByStudentId(studentId));
    }
}
