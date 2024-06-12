package com.recipesapi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.recipesapi.model.Ingredient;

@Service
public interface IngredientService {
    Ingredient getIngredientById(int ingredientId);

    ResponseEntity<String> updateIngredient(int id, Ingredient updateIngredient);

    ResponseEntity<String> deleteIngredient(Integer idIngredient);

    List<Ingredient> searchIngredients(String keyword);
}
