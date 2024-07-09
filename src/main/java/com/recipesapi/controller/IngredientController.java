package com.recipesapi.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.recipesapi.config.DemoMode;
import com.recipesapi.exception.DemoModeException;
import com.recipesapi.model.Ingredient;
import com.recipesapi.repository.IngredientRepository;
import com.recipesapi.service.IngredientService;
import com.recipesapi.utility.FormatResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "CRUD REST APIs for Ingredient Resource", description = "CUSTOMERS CRUD REST APIs - Create Ingredient, Update Ingredient, Get Ingredient, Get All Ingredients, Delete Ingredient")
@RestController
@RequestMapping("/api/v1/ingredients/")
public class IngredientController {

    private final IngredientRepository repository;
    private final IngredientService service;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DemoMode demoMode;

    public IngredientController(IngredientRepository repository, IngredientService service) {
        this.repository = repository;
        this.service = service;
    }

    // Get all Ingredients Rest Api
    // http://localhost:8081/api/v1/ingredients
    @Operation(summary = "Get all ingredients", description = "Retrieve a list of all ingredients")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @GetMapping("/")
    public Iterable<Ingredient> list() {
        return repository.findAll();
    }
    //

    // Get single Ingredient Rest Api (get id by url)
    // http://localhost:8081/api/v1/ingredients/1
    @Operation(summary = "Get Ingredient By ID REST API", description = "Get Ingredient By ID REST API is used to get a single Ingredient from the database, get id by url")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @GetMapping("/{id}")
    public Ingredient getById(@PathVariable Integer id) {
        return repository.findById(id).get();
    }
    //

    // Add new Ingredient Rest Api
    // http://localhost:8081/api/v1/ingredients/add
    @Operation(summary = "Crate new Ingredient REST API", description = "Save new Ingredient on database")
    @ApiResponse(responseCode = "201", description = "HTTP Status 201 Created")
    @PostMapping("/add")
    public ResponseEntity<FormatResponse> add(@Valid @RequestBody Ingredient p) {
         if (demoMode.isEnabled()) {
            throw new DemoModeException();
        }
        repository.save(p);
        return new ResponseEntity<FormatResponse>(new FormatResponse("Ingredient addedd successfully"),
                HttpStatus.CREATED);
    }
    //

    // Update Ingredient Rest Api
    // http://localhost:8081/api/v1/ingredients/1
    @Operation(summary = "Update Ingredient REST API", description = "Update Ingredient on database")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PatchMapping("/{id}")
    public ResponseEntity<FormatResponse> update(@PathVariable Integer id, @RequestBody Ingredient updatedIngredient) {
        if (demoMode.isEnabled()) {
            throw new DemoModeException();
        }
        return service.updateIngredient(id, updatedIngredient);
    }
    //

    // Delete Ingredient Rest Api
    // http://localhost:8081/api/v1/ingredients/1
    @Operation(summary = "Delete Ingredient REST API", description = "Delete Ingredient on database")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @DeleteMapping("/{id}")
    public ResponseEntity<FormatResponse> delete(@PathVariable Integer id) {
        if (demoMode.isEnabled()) {
            throw new DemoModeException();
        }
        service.deleteIngredient(id);
        return new ResponseEntity<FormatResponse>(new FormatResponse("Ingredient deleted successfully"), HttpStatus.OK);
    }
    //

    // Search Ingredient Rest Api
    // http://localhost:8081/api/v1/ingredients/search
    @Operation(summary = "Search Ingredient REST API", description = "Search Ingredient on database by filter")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @GetMapping("/search")
    public List<Ingredient> searchIngredients(@RequestParam("keyword") String keyword) {
        List<Ingredient> ingredients = service.searchIngredients(keyword);
        return ingredients;
    }
    //

}
