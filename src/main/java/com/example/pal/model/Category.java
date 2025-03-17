package com.example.pal.model;

<<<<<<< HEAD
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
=======
import jakarta.persistence.*;
>>>>>>> bae4fce61cce078c43def1a82a08eec1c0d3bf14
import lombok.Data;

@Data
@Entity
@Table(name = "categories")
public class Category {
<<<<<<< HEAD
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
=======
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

>>>>>>> bae4fce61cce078c43def1a82a08eec1c0d3bf14
}
