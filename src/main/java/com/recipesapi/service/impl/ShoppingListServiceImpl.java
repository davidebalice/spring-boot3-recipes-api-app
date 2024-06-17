package com.recipesapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.recipesapi.exception.ResourceNotFoundException;
import com.recipesapi.model.ShoppingList;
import com.recipesapi.repository.ShoppingListRepository;
import com.recipesapi.service.ShoppingListService;
import com.recipesapi.utility.FormatResponse;

@Service
public class ShoppingListServiceImpl implements ShoppingListService {

    private final ShoppingListRepository repository;

    public ShoppingListServiceImpl(ShoppingListRepository repository) {
        this.repository = repository;
    }

    @Override
    public ShoppingList addIngredient(ShoppingList shoppingList) {

        shoppingList.setTitle(shoppingList.getTitle());
        shoppingList.setQuantity(shoppingList.getQuantity());
        return repository.save(shoppingList);
    }

    @Override
    public List<ShoppingList> addIngredients(List<ShoppingList> ingredients) {
        for (ShoppingList ingredient : ingredients) {
            Optional<ShoppingList> existingIngredient = repository.findByTitle(ingredient.getTitle());

            if (existingIngredient.isPresent()) {
                ShoppingList existing = existingIngredient.get();
                existing.setQuantity(existing.getQuantity() + ingredient.getQuantity());
                repository.save(existing);
            } else {
                repository.save(ingredient);
            }
        }
        return repository.findAll();
    }

    @Override
    public ShoppingList getShoppingListById(int slId) {
        return repository.findById(slId).orElseThrow(
                () -> new ResourceNotFoundException("Ingredient", "id"));
    }

    @Override
    public ResponseEntity<FormatResponse> updateShoppingList(int id, ShoppingList updatedShoppingList) {
        try {
            if (!repository.existsById(id)) {
                return new ResponseEntity<FormatResponse>(new FormatResponse("Shopping list item not found"),
                        HttpStatus.NOT_FOUND);
            }

            ShoppingList existingShoppingList = repository.findById(id).get();

            if (updatedShoppingList.getTitle() != null) {
                existingShoppingList.setTitle(updatedShoppingList.getTitle());
            }
            if (updatedShoppingList.getQuantity() > 1) {
                existingShoppingList.setQuantity(updatedShoppingList.getQuantity());
            }

            repository.save(existingShoppingList);

            return new ResponseEntity<FormatResponse>(new FormatResponse("Shopping list item updated successfully!"),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<FormatResponse>(new FormatResponse("Error updating item"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<FormatResponse> deleteShoppingList(Integer recipeId) {
        Optional<ShoppingList> pOptional = repository.findById(recipeId);
        if (pOptional.isPresent()) {
            ShoppingList p = pOptional.get();
            repository.delete(p);
            return new ResponseEntity<FormatResponse>(new FormatResponse("Shopping list item deleted successfully"),
                    HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Shopping list", "id");
        }
    }

    @Override
    public List<ShoppingList> getAllShoppingLists() {
        return repository.findAll();
    }
}
