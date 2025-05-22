package com.example.pal.service;

import com.example.pal.model.Course;
import com.example.pal.model.User;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PDFService {
    public byte[] generateSimplePdf(String title, String content) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Inicializar PDF writer
        PdfWriter writer = new PdfWriter(baos);

        // Inicializar PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Inicializar document
        Document document = new Document(pdf);

        try {
            // Añadir título
            document.add(new Paragraph(title).setBold().setFontSize(16)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

            // Añadir fecha y hora
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            document.add(new Paragraph("Generado: " + now.format(formatter))
                    .setFontSize(10)
                    .setItalic())
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT);
            // Añadir contenido principal
            document.add(new Paragraph(content)
                    .setMarginTop(20)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    /**
     * Generar un PDF de certificado para el estudiante que ha completado el curso.
     */
    public byte[] generateCertificatePdf(User user, Course course) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Crear el documento PDF en memoria
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Añadir contenido al PDF
        document.add(new Paragraph("Certificado de Curso").setBold().setFontSize(20));
        document.add(new Paragraph("Este certificado certifica que " + user.getUsername() + " ha completado el curso: "
                + course.getTitle()));
        document.add(new Paragraph("Fecha de emisión: " + java.time.LocalDate.now()));

        document.close();

        return baos.toByteArray();
    }
}