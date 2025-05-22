package com.example.pal.repository;

import com.example.pal.model.Certificate;
import com.example.pal.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    // certificados por id de estudiante
    // obtener certificados por id estudiante /varios, devuelve una lista}
    List<Certificate> findByUser(User user);

}
