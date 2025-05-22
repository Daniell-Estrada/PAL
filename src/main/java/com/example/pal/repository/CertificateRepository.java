package com.example.pal.repository;

import com.example.pal.model.Certificate;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
  List<Certificate> findByUserId(Long userId);
}
