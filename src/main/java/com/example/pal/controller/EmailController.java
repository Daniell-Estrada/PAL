package com.example.pal.controller;

import com.example.pal.service.EmailService;
import com.example.pal.service.PDFService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private PDFService pdfService;

    public EmailController() {
    }

    /**
     * Send a simple email
     * @param to The recipient's email address
     * @param subject The subject of the email
     * @param text The body of the email
     * @return
     */
    @GetMapping("/send-email")
    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        try {
            this.emailService.sendSimpleEmail(to, subject, text);
            return "Correo enviado exitosamente a: " + to;
        } catch (Exception e) {
            return "Error al enviar el correo: " + e.getMessage();
        }
    }

    /**
     * Send an email with a PDF attachment (for testing purposes)
     * @param to The recipient's email address
     * @param subject The subject of the email
     * @param emailText The body of the email
     * @param titlePdf The title of the PDF
     * @param contentPdf The content of the PDF
     * @return
     */
    @GetMapping("/send-email-pdf")
    public String sendPdfEmail(@RequestParam String to, @RequestParam String subject,
            @RequestParam String emailText, @RequestParam String titlePdf,
            @RequestParam String contentPdf) {

        try {
            byte[] pdfBytes = pdfService.generateSimplePdf(titlePdf, contentPdf);

            emailService.sendEmailWithAttachment(to, subject, emailText, pdfBytes, "documento.pdf");

            return "Correo con PDF adjunto enviado exitosamente a: " + to;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al enviar el correo con PDF: " + e.getMessage();
        }
    }

}
