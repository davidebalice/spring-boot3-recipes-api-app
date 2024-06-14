package com.recipesapi.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.recipesapi.dto.CategoryDto;
import com.recipesapi.model.Category;
import com.recipesapi.utility.FormatResponse;
@Service
public interface CategoryService {
    CategoryDto getCategoryById(int categoryId);
    ResponseEntity<FormatResponse> updateCategory(int id, Category updateCategory);
    ResponseEntity<FormatResponse> deleteCategory(Integer idCategory);
    List<Category> searchCategories(String keyword);
}
