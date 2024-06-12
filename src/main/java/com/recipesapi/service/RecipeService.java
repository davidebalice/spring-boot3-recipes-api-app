package com.recipesapi.service;

import com.recipesapi.dto.RecipeDto;
import com.recipesapi.model.Recipe;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Service
public interface RecipeService {
    Recipe addRecipe(RecipeDto p);
    Recipe getRecipeById(int productId);
    ResponseEntity<String> updateRecipe(int id, RecipeDto updatedRecipe);
    ResponseEntity<String> deleteRecipe(Integer idRecipe);
    List<Recipe> searchRecipes(String keyword);
    List<Recipe> searchRecipesByCategoryId(int categoryId);
    List<Recipe> getAllRecipes();
}
