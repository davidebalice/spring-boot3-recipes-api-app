package com.recipesapi.mapper;

import com.recipesapi.dto.RecipeDto;
import com.recipesapi.model.Category;
import com.recipesapi.model.Recipe;

public class RecipeMapper {

    // Convert Product JPA Entity into ProductDto
    public static RecipeDto mapToProductDto(Recipe product) {
        RecipeDto productDto = new RecipeDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        //productDto.setPrice(product.getPrice());
        productDto.setIdCategory(product.getCategory().getId());

        return productDto;
    }

    // Convert ProductDto into Product JPA Entity
    public static Recipe mapToProduct(RecipeDto productDto) {
        Recipe product = new Recipe();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
      //  product.setPrice(productDto.getPrice());

        Category category = new Category();
        category.setId(product.getCategory().getId());
        category.setName(product.getCategory().getName());
        category.setDescription(product.getCategory().getDescription());
        product.setCategory(category);

        return product;
    }
}
