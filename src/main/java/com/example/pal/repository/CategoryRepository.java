package com.example.pal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pal.model.Category;

@Repository
<<<<<<< HEAD
public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByName(String name);
=======
public interface CategoryRepository extends JpaRepository<Category, Long>{
	Optional<Category> findByName(String name);
>>>>>>> bae4fce61cce078c43def1a82a08eec1c0d3bf14
}
