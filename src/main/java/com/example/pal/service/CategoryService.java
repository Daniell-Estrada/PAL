package com.example.pal.service;

import com.example.pal.dto.category.CategoryDTO;
import com.example.pal.dto.category.CreateCategoryDTO;
import com.example.pal.model.Category;
import com.example.pal.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;
  @Autowired
  private ModelMapper modelMapper;

  /**
   * Create a new category
   *
   * @param categoryDTO
   * @return
   */
  public CategoryDTO createCategory(CreateCategoryDTO categoryDTO) {
    if (categoryRepository.existsByName(categoryDTO.getName())) {
      throw new IllegalArgumentException("La categoría con el nombre '" + categoryDTO.getName() + "' ya existe.");
    }
    Category category = new Category();
    category.setName(categoryDTO.getName());

    Category savedCategory = categoryRepository.save(category);
    return modelMapper.map(savedCategory, CategoryDTO.class);
  }

  /**
   * Get all categories
   *
   * @return A list of all categories, instances of CategoryDTO
   */
  public List<CategoryDTO> getAllCategories() {
    List<Category> categories = categoryRepository.findAll();
    return categories.stream()
        .map(category -> modelMapper.map(category, CategoryDTO.class))
        .collect(Collectors.toList());
  }

  /**
   * Get a category by its ID
   *
   * @param id The ID of the category to fetch
   * @return The category with the given ID, an instance of CategoryDTO
   */
  public Optional<CategoryDTO> getCategoryById(Long id) {
    return categoryRepository
        .findById(id)
        .map(category -> modelMapper.map(category, CategoryDTO.class));
  }

  /**
   * Update a category
   *
   * @param id          The ID of the category to update
   * @param categoryDTO The new details for the category
   * @return The updated category, an instance of CategoryDTO
   */
  public CategoryDTO updateCategory(Long id, CreateCategoryDTO categoryDTO) {
    Category category = categoryRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Category not found"));

    if (categoryRepository.existsByName(categoryDTO.getName()) &&
        !category.getName().equals(categoryDTO.getName())) {
      throw new IllegalArgumentException("La categoría con el nombre '" + categoryDTO.getName() + "' ya existe.");
    }
    category.setName(categoryDTO.getName());

    Category updatedCategory = categoryRepository.save(category);

    return modelMapper.map(updatedCategory, CategoryDTO.class);
  }

  /**
   * Delete a category
   *
   * @param id The ID of the category to delete
   */
  public void deleteCategory(Long id) {
    if (!categoryRepository.existsById(id)) {
      throw new RuntimeException("Category not found");
    }
    categoryRepository.deleteById(id);
  }

  /**
   * Get categories by name
   *
   * @param name The name of the category to search for
   * @return A list of categories with the given name, instances of CategoryDTO
   */
  public List<CategoryDTO> getCategoriesByName(String name) {
    Optional<Category> categoryOpt = categoryRepository.findByName(name);
    return categoryOpt
        .map(category -> List.of(modelMapper.map(category, CategoryDTO.class)))
        .orElse(List.of());
  }
}
