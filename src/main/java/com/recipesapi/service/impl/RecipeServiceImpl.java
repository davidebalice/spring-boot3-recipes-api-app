package com.recipesapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.recipesapi.dto.RecipeDto;
import com.recipesapi.exception.ResourceNotFoundException;
import com.recipesapi.model.Category;
import com.recipesapi.model.Recipe;
import com.recipesapi.repository.CategoryRepository;
import com.recipesapi.repository.RecipeRepository;
import com.recipesapi.service.RecipeService;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository repository;
    private final CategoryRepository categoryRepository;

    public RecipeServiceImpl(RecipeRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Recipe addRecipe(RecipeDto recipeDto) {
        Category category = categoryRepository.findById(recipeDto.getIdCategory())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Recipe recipe = new Recipe();
        recipe.setName(recipeDto.getName());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setCategory(category);
        //recipe.setPrice(recipeDto.getPrice());
        // recipe.setImageUrl(recipeDto.getImageUrl());
        // recipe.setActive(recipeDto.isActive());

        return repository.save(recipe);
    }

    @Override
    public Recipe getRecipeById(int recipeId) {
        return repository.findById(recipeId).orElseThrow(
                () -> new ResourceNotFoundException("Recipe", "id"));
    }

    @Override
    public Recipe getRecipeBySku(String sku) {
        return repository.findBySku(sku).orElseThrow(
                () -> new ResourceNotFoundException("Recipe", "sku"));
    }

    @Override
    public ResponseEntity<String> updateRecipe(int id, RecipeDto updatedRecipe) {
        try {
            if (!repository.existsById(id)) {
                return new ResponseEntity<>("Recipe not found", HttpStatus.NOT_FOUND);
            }

            Recipe existingRecipe = repository.findById(id).get();

            if (updatedRecipe.getName() != null) {
                existingRecipe.setName(updatedRecipe.getName());
            }
            if (updatedRecipe.getDescription() != null) {
                existingRecipe.setDescription(updatedRecipe.getDescription());
            }
            if (updatedRecipe.getIdCategory() >= 1) {
                Category category = categoryRepository.findById(updatedRecipe.getIdCategory())
                        .orElseThrow(() -> new RuntimeException("Category not found"));

                existingRecipe.setCategory(category);
            }
            /*
            if (updatedRecipe.getPrice() != 0.0) {
                existingRecipe.setPrice(updatedRecipe.getPrice());
            }
            */

            repository.save(existingRecipe);

            return new ResponseEntity<>("Recipe updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating recipe", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteRecipe(Integer recipeId) {
        Optional<Recipe> pOptional = repository.findById(recipeId);
        if (pOptional.isPresent()) {
            Recipe p = pOptional.get();
            repository.delete(p);
            return new ResponseEntity<>("Recipe deleted successfully", HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Recipe", "id");
        }
    }

    @Override
    public List<Recipe> searchRecipes(String keyword) {
        return repository.searchRecipes(keyword);
    }

    @Override
    public List<Recipe> searchRecipesByCategoryId(int categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return repository.findAll();
    }
}
