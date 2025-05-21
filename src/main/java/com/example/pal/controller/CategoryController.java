package com.example.pal.controller;

import com.example.pal.dto.category.CategoryDTO;
import com.example.pal.dto.category.CreateCategoryDTO;
import com.example.pal.service.CategoryService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @PostMapping("/create")
  public ResponseEntity<String> createCategory(@RequestBody CreateCategoryDTO categoryDTO) {
    try {
      CategoryDTO category = categoryService.createCategory(categoryDTO);
      return ResponseEntity.status(201)
          .body("Categoría creada exitosamente: " + category.getName());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(400).body("Error: " + e.getMessage());
    }
  }

  @GetMapping("/all")
  public ResponseEntity<List<CategoryDTO>> getAllCategories() {
    return ResponseEntity.ok(categoryService.getAllCategories());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
    Optional<CategoryDTO> category = categoryService.getCategoryById(id);
    return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<String> updateCategory(@PathVariable("id") Long id,
      @RequestBody CreateCategoryDTO categoryDTO) {
    try {
      CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
      return ResponseEntity.ok("Categoría actualizada exitosamente: " + updatedCategory.getName());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(400).body("Error: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/by-name")
  public ResponseEntity<List<CategoryDTO>> getCategoriesByName(@RequestParam String name) {
    List<CategoryDTO> categories = categoryService.getCategoriesByName(name);
    if (categories.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(categories);
  }
}
