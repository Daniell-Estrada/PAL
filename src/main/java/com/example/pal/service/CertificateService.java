package com.example.pal.service;

import com.example.pal.dto.certificado.CertificateDTO;
import com.example.pal.model.Certificate;
import com.example.pal.model.Course;
import com.example.pal.model.Enrollment;
import com.example.pal.model.User;
import com.example.pal.repository.CertificateRepository;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.EnrollmentRepository;
import com.example.pal.repository.UserRepository;

import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private PDFService pdfService;

    @Autowired
    private EmailService emailService;

    /**
     * Generar certificado para un estudiante que ha completado un curso.
     */
    public Certificate generateCertificate(Long userId, Long courseId) throws IOException, MessagingException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(user, course)
                .orElseThrow(() -> new RuntimeException("El usuario no está inscrito en este curso"));

        if (!"completado".equalsIgnoreCase(enrollment.getStatus())) {
            throw new RuntimeException("El curso no ha sido completado por el estudiante");
        }

        Certificate certificate = new Certificate();
        certificate.setUser(user);
        certificate.setCourse(course);
        certificate.setIssueDate(LocalDate.now());

        byte[] pdfBytes = pdfService.generateCertificatePdf(user, course);

        String certificateFile = "certificates/" + user.getId() + "_" + course.getId() + ".pdf";
        Files.createDirectories(Paths.get("certificates"));
        Files.write(Paths.get(certificateFile), pdfBytes);
        certificate.setCertificateFile(certificateFile);
        certificateRepository.save(certificate);

        emailService.sendEmailWithAttachment(
                user.getEmail(),
                "Tu Certificado de Curso",
                "Felicidades por completar el curso! Aquí está tu certificado.",
                pdfBytes,
                "certificado_" + course.getTitle() + ".pdf");

        return certificate;
    }

    /**
     * Descargar el certificado en formato PDF.
     */
    public byte[] getCertificatePdf(Long certificateId) throws IOException {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new RuntimeException("Certificado no encontrado"));
        // Leer el archivo PDF desde el sistema de archivos
        return Files.readAllBytes(Paths.get(certificate.getCertificateFile()));
    }

    /**
     * Obtener todos los certificados generados.
     */
    public List<CertificateDTO> getAllCertificates() {
        List<Certificate> certificates = certificateRepository.findAll();
        List<CertificateDTO> dtos = new ArrayList<>();
        for (Certificate cert : certificates) {
            CertificateDTO dto = new CertificateDTO();
            dto.setId(cert.getId());
            dto.setStudentName(cert.getUser().getUsername());
            dto.setCourseTitle(cert.getCourse().getTitle());
            dto.setIssueDate(cert.getIssueDate());
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * Eliminar un certificado.
     */
    public void deleteCertificate(Long certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new RuntimeException("Certificado no encontrado"));  
        certificateRepository.delete(certificate);
    }
}
