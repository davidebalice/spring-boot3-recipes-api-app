package com.recipesapi.mapper;

import org.hibernate.mapping.Set;
import org.springframework.stereotype.Component;

import com.recipesapi.dto.IngredientDto;
import com.recipesapi.dto.RecipeDto;
import com.recipesapi.model.Category;
import com.recipesapi.model.Ingredient;
import com.recipesapi.model.Recipe;

public class RecipeMapper {

    // Convert Recipe JPA Entity into RecipeDto
    public static RecipeDto mapToRecipeDto(Recipe recipe) {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(recipe.getId());
        recipeDto.setTitle(recipe.getTitle());
        recipeDto.setDescription(recipe.getDescription());
        recipeDto.setIdCategory(recipe.getCategory().getId());
        return recipeDto;
    }

    // Convert RecipeDto into Recipe JPA Entity
    public static Recipe mapToRecipe(RecipeDto recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeDto.getId());
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());

        Category category = new Category();
        category.setId(recipe.getCategory().getId());
        category.setName(recipe.getCategory().getName());
        category.setDescription(recipe.getCategory().getDescription());
        recipe.setCategory(category);

        return recipe;
    }
}


