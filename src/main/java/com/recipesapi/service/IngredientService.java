package com.recipesapi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.recipesapi.model.Ingredient;
import com.recipesapi.utility.FormatResponse;

@Service
public interface IngredientService {
    Ingredient getIngredientById(int ingredientId);
    ResponseEntity<FormatResponse> updateIngredient(int id, Ingredient updateIngredient);
    ResponseEntity<FormatResponse> deleteIngredient(Integer idIngredient);
    List<Ingredient> searchIngredients(String keyword);
}
