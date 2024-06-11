package com.recipesapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.recipesapi.dto.CategoryDto;
import com.recipesapi.exception.ResourceNotFoundException;
import com.recipesapi.model.Category;
import com.recipesapi.repository.CategoryRepository;
import com.recipesapi.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public CategoryDto getCategoryById(int categoryId) {
        Category category = repository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id"));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public ResponseEntity<String> updateCategory(int categoryId, Category updateCategory) {
        try {
            if (!repository.existsById(categoryId)) {
                throw new ResourceNotFoundException("Category", "id");
            }

            Category existingCategory = repository.findById(categoryId).orElse(null);

            if (updateCategory.getName() != null) {
                existingCategory.setName(updateCategory.getName());
            }

            repository.save(existingCategory);

            return new ResponseEntity<>("Category updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating category", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteCategory(Integer categoryId) {
        Optional<Category> pOptional = repository.findById(categoryId);
        if (pOptional.isPresent()) {
            Category c = pOptional.get();
            repository.delete(c);
            return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Category", "id");
        }
    }
    

    @Override
    public List<Category> searchCategories(String keyword) {
        return repository.searchCategories(keyword);
    }
}
