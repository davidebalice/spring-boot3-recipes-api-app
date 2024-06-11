package com.recipesapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recipesapi.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    void deleteById(Optional<Recipe> p);

    // JPQL query
    @Query("SELECT p FROM Recipe p " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%')) ")
    List<Recipe> searchRecipes(@Param("keyword") String keyword);

    // Native sql query
    /*
     * @Query(value = "SELECT * FROM api_recipes p WHERE " +
     * "p.name LIKE CONCAT('%',:keyword, '%')" +
     * "OR p.description LIKE CONCAT('%', :keyword, '%')" +
     * "OR p.sku LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
     * List<Recipe> searchRecipes(@Param("keyword") String keyword);
     */

    List<Recipe> findByCategoryId(int categoryId);

    @Query("SELECT p FROM Recipe p " +
            "WHERE LOWER(p.sku) LIKE LOWER(CONCAT('%', :sku, '%')) ")
    Optional<Recipe> findBySku(@Param("sku") String sku);

}
