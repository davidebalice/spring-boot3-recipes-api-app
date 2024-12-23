package com.recipesapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.recipesapi.exception.ResourceNotFoundException;
import com.recipesapi.model.Ingredient;
import com.recipesapi.repository.IngredientRepository;
import com.recipesapi.service.IngredientService;
import com.recipesapi.utility.FormatResponse;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository repository;

    public IngredientServiceImpl(IngredientRepository repository) {
        this.repository = repository;
    }

    @Override
    public Ingredient getIngredientById(int ingredientId) {
        return repository.findById(ingredientId).orElseThrow(
                () -> new ResourceNotFoundException("Ingredient", "id"));
    }

    @Override
    public ResponseEntity<FormatResponse> updateIngredient(int id, Ingredient updateIngredient) {
        try {
            if (!repository.existsById(id)) {
                return new ResponseEntity<FormatResponse>(new FormatResponse("Ingredient not found"), HttpStatus.NOT_FOUND);
            }

            Ingredient existingIngredient = repository.findById(id).get();

            if (updateIngredient.getTitle() != null) {
                existingIngredient.setTitle(updateIngredient.getTitle());
            }
           

            repository.save(existingIngredient);

            return new ResponseEntity<FormatResponse>(new FormatResponse("Ingredient updated successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<FormatResponse>(new FormatResponse("Error updating ingredient"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<FormatResponse> deleteIngredient(Integer ingredientId) {
        Optional<Ingredient> pOptional = repository.findById(ingredientId);
        if (pOptional.isPresent()) {
            Ingredient c = pOptional.get();
            repository.delete(c);
            return new ResponseEntity<FormatResponse>(new FormatResponse("Ingredient deleted successfully"), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Ingredient", "id");
        }
    }

    @Override
    public List<Ingredient> searchIngredients(String keyword) {
        return repository.searchIngredients(keyword);
    }
}
