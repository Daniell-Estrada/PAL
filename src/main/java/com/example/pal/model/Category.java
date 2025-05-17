package com.example.pal.model;

//import com.example.pal.validation.UniqueCategoryName;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "categories")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //@UniqueCategoryName
  @Column(nullable = false, unique = true)
  private String name;

}
