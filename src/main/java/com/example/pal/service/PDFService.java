package com.example.pal.service;

import com.example.pal.model.Course;
import com.example.pal.model.User;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
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

        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        String logoPath = "src/assets/logo.png";
        try {
            Image logo = new Image(ImageDataFactory.create(logoPath));
            logo.setWidth(120);
            logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(logo);
        } catch (Exception e) {
        }
        // Título principal
        document.add(new Paragraph("Certificate of Completion")
                .setBold()
                .setFontSize(28)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20)
                .setMarginBottom(30));

        // Mensaje principal
        document.add(new Paragraph("This is to certify that")
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10));

        // Nombre del estudiante
        document.add(new Paragraph(user.getUsername())
                .setBold()
                .setFontSize(22)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(new DeviceRgb(44, 162, 178))
                .setMarginBottom(10));

        // Mensaje de curso
        document.add(new Paragraph("has successfully completed the course")
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10));

        // Título del curso
        document.add(new Paragraph(course.getTitle())
                .setBold()
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(com.itextpdf.kernel.colors.ColorConstants.DARK_GRAY)
                .setMarginBottom(20));

        // Fecha de emisión
        document.add(new Paragraph("Issued on: " + java.time.LocalDate.now())
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(30));

        // Firma o pie de página (opcional)
        document.add(new Paragraph("Platform Team")
                .setItalic()
                .setFontSize(12)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(40));

        document.close();
        return baos.toByteArray();
    }
}