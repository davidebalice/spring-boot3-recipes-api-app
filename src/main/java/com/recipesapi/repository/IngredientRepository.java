package com.recipesapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recipesapi.model.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    void deleteById(Optional<Ingredient> p);

    @Query("SELECT c FROM Ingredient c " +
            "WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) ")
    List<Ingredient> searchCustomers(@Param("keyword") String keyword);

}
