package com.recipesapi.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.recipesapi.dto.IngredientDto;
import com.recipesapi.dto.RecipeDto;
import com.recipesapi.exception.ResourceNotFoundException;
import com.recipesapi.model.Category;
import com.recipesapi.model.Ingredient;
import com.recipesapi.model.Recipe;
import com.recipesapi.repository.CategoryRepository;
import com.recipesapi.repository.RecipeRepository;
import com.recipesapi.service.RecipeService;
import com.recipesapi.utility.FormatResponse;

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
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setCategory(category);
        // recipe.setPrice(recipeDto.getPrice());
        // recipe.setImageUrl(recipeDto.getImageUrl());
        // recipe.setActive(recipeDto.isActive());

        if (recipeDto.getIngredients() != null) {
            for (IngredientDto ingredientDto : recipeDto.getIngredients()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setTitle(ingredientDto.getTitle());

                ingredient.setQuantity(ingredientDto.getQuantity());
                ingredient.setRecipe(recipe);

                System.out.println(ingredientDto.getTitle());
                System.out.println(ingredientDto.getQuantity());

                recipe.addIngredient(ingredient);
            }
        }
        System.out.println(recipe.getIngredients());
        return repository.save(recipe);
    }

    @Override
    public Recipe getRecipeById(int recipeId) {
        return repository.findById(recipeId).orElseThrow(
                () -> new ResourceNotFoundException("Recipe", "id"));
    }

    @Override
    public ResponseEntity<FormatResponse> updateRecipe(int id, RecipeDto updatedRecipe) {
        try {
            if (!repository.existsById(id)) {
                return new ResponseEntity<FormatResponse>(new FormatResponse("Recipe not found"), HttpStatus.NOT_FOUND);
            }

            Recipe existingRecipe = repository.findById(id).get();

            if (updatedRecipe.getTitle() != null) {
                existingRecipe.setTitle(updatedRecipe.getTitle());
            }
            if (updatedRecipe.getDescription() != null) {
                existingRecipe.setDescription(updatedRecipe.getDescription());
            }

            System.out.println(updatedRecipe.getImageUrl());
            
            if (updatedRecipe.getImageUrl() != null) {
                existingRecipe.setImageUrl(updatedRecipe.getImageUrl());
            }
            if (updatedRecipe.getIdCategory() >= 1) {
                Category category = categoryRepository.findById(updatedRecipe.getIdCategory())
                        .orElseThrow(() -> new RuntimeException("Category not found"));

                existingRecipe.setCategory(category);
            }

            if (updatedRecipe.getIngredients() != null) {
                Set<Ingredient> existingIngredients = existingRecipe.getIngredients();

                Map<String, Ingredient> existingIngredientMap = existingIngredients.stream()
                        .collect(Collectors.toMap(Ingredient::getTitle, ingredient -> ingredient));

                for (IngredientDto updatedIngredientDto : updatedRecipe.getIngredients()) {
                    String ingredientTitle = updatedIngredientDto.getTitle();
                    int ingredientQuantity = updatedIngredientDto.getQuantity();

                    if (existingIngredientMap.containsKey(ingredientTitle)) {
                        Ingredient existingIngredient = existingIngredientMap.get(ingredientTitle);
                        existingIngredient.setQuantity(ingredientQuantity);
                    } else {
                        Ingredient newIngredient = new Ingredient();
                        newIngredient.setTitle(ingredientTitle);
                        newIngredient.setQuantity(ingredientQuantity);
                        newIngredient.setRecipe(existingRecipe);
                        existingIngredients.add(newIngredient);
                    }
                }

                existingIngredients.removeIf(ingredient -> updatedRecipe.getIngredients().stream()
                        .noneMatch(
                                updatedIngredientDto -> updatedIngredientDto.getTitle().equals(ingredient.getTitle())));

                existingRecipe.setIngredients(existingIngredients);

            }

            repository.save(existingRecipe);

            return new ResponseEntity<FormatResponse>(new FormatResponse("Recipe updated successfully!"),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<FormatResponse>(new FormatResponse("Error updating recipe"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<FormatResponse> deleteRecipe(Integer recipeId) {
        Optional<Recipe> pOptional = repository.findById(recipeId);
        if (pOptional.isPresent()) {
            Recipe p = pOptional.get();
            repository.delete(p);
            return new ResponseEntity<FormatResponse>(new FormatResponse("Recipe deleted successfully"), HttpStatus.OK);
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
