package com.recipesapi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.recipesapi.model.ShoppingList;
import com.recipesapi.utility.FormatResponse;

@Service
public interface ShoppingListService {
    ShoppingList addIngredient(ShoppingList sl);

    List<ShoppingList> addIngredients(List<ShoppingList> sl);

    ShoppingList getShoppingListById(int slId);

    ResponseEntity<FormatResponse> updateShoppingList(int id, ShoppingList updatedShoppingList);

    ResponseEntity<FormatResponse> deleteShoppingList(Integer idShoppingList);

    List<ShoppingList> getAllShoppingLists();
}
