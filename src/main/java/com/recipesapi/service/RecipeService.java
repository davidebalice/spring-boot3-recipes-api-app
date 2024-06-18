package com.recipesapi.service;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.recipesapi.dto.RecipeDto;
import com.recipesapi.model.Recipe;
import com.recipesapi.utility.FormatResponse;

import jakarta.servlet.http.HttpServletRequest;

@Service
public interface RecipeService {

    Recipe addRecipe(RecipeDto recipeDto) throws IOException;

    Recipe addRecipe2(RecipeDto recipeDto, MultipartFile imageFile) throws IOException;

    Recipe getRecipeById(int recipeId);

    ResponseEntity<FormatResponse> updateRecipe(int id, RecipeDto updatedRecipe);

    ResponseEntity<FormatResponse> deleteRecipe(Integer idRecipe);

    List<Recipe> searchRecipes(String keyword);

    List<Recipe> searchRecipesByCategoryId(int categoryId);

    List<Recipe> getAllRecipes();

    String uploadImage(int id, MultipartFile multipartFile, String uploadPath) throws IOException;

    ResponseEntity<Resource> downloadImage(String fileName, HttpServletRequest request, String uploadPath)
            throws IOException;
}
