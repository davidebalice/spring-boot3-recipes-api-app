package com.recipesapi.service;

import com.recipesapi.dto.RecipeDto;
import com.recipesapi.model.Recipe;
import com.recipesapi.utility.FormatResponse;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Service
public interface RecipeService {
    Recipe addRecipe(RecipeDto p);
    Recipe getRecipeById(int productId);
    ResponseEntity<FormatResponse> updateRecipe(int id, RecipeDto updatedRecipe);
    ResponseEntity<FormatResponse> deleteRecipe(Integer idRecipe);
    List<Recipe> searchRecipes(String keyword);
    List<Recipe> searchRecipesByCategoryId(int categoryId);
    List<Recipe> getAllRecipes();
}
