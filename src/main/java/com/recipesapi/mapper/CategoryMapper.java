package com.recipesapi.mapper;

import com.recipesapi.dto.CategoryDto;
import com.recipesapi.model.Category;

public class CategoryMapper {

    // Convert Category JPA Entity into CategoryDto
    public static CategoryDto mapToCategoryDto(Category p) {
        CategoryDto CategoryDto = new CategoryDto(
                p.getId(),
                p.getName(),
                p.getDescription());
        return CategoryDto;
    }

    // Convert CategoryDto into Category JPA Entity
    public static Category mapToCategory(Category category) {
        Category p = new Category(
                category.getId(),
                category.getName(),
                category.getDescription(), 0, null, null);
        return p;
    }
}
