package com.recipesapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipesapi.model.ShoppingList;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Integer> {
    Optional<ShoppingList> findByTitle(String title);
    void deleteById(Optional<ShoppingList> p);
}
