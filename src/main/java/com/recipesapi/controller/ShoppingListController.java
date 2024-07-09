package com.recipesapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.recipesapi.model.ShoppingList;
import com.recipesapi.repository.ShoppingListRepository;
import com.recipesapi.service.ShoppingListService;
import com.recipesapi.utility.FormatResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "CRUD REST APIs for Shopping list Resource", description = "RECIPES CRUD REST APIs - Create ShoppingList, Update ShoppingList, Get ShoppingList, Get All ShoppingLists, Delete ShoppingList")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/shoppinglist/")
public class ShoppingListController {

    private final ShoppingListRepository repository;
    private final ShoppingListService service;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DemoMode demoMode;

    public ShoppingListController(ShoppingListRepository repository, ShoppingListService service) {
        this.repository = repository;
        this.service = service;
    }

    // Get all ShoppingLists Rest Api
    // http://localhost:8081/api/v1/shoppinglist
    @Operation(summary = "Get all ingredients in shopping list", description = "Retrieve a list of all ingredients in shopping list")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @GetMapping("/")
    public ResponseEntity<Iterable<ShoppingList>> list() {
        Iterable<ShoppingList> shoppingLists = repository.findAll();
        List<ShoppingList> shoppingListsDto = new ArrayList<>();
        for (ShoppingList shoppingList : shoppingLists) {
            ShoppingList shoppingListDto = modelMapper.map(shoppingList, ShoppingList.class);
            shoppingListsDto.add(shoppingListDto);
        }
        return ResponseEntity.ok(shoppingListsDto);
    }
    //

    // Get single ShoppingList Rest Api by Id (get id by url)
    // http://localhost:8081/api/v1/shoppinglist/1
    @Operation(summary = "Get ingredient By ID REST API", description = "Get ingredient By ID REST API is used to get a single ingredient from the database, get id by url")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @GetMapping("/{id}")
    public ResponseEntity<ShoppingList> getById(@PathVariable Integer id) {
        ShoppingList shoppingList = service.getShoppingListById(id);
        System.out.println(shoppingList);
        if (shoppingList != null) {
            ShoppingList shoppingListDto = modelMapper.map(shoppingList, ShoppingList.class);
            return new ResponseEntity<>(shoppingListDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //

    // Get single ShoppingList Rest Api (get id by querystring)
    // http://localhost:8081/api/v1/shoppinglist?id=1
    @Operation(summary = "Get ShoppingList By ID REST API", description = "Get ShoppingList By ID REST API is used to get a single ShoppingList from the database, get id by querystring")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @GetMapping("/shoppingList")
    public ShoppingList getByIdQs(@RequestParam Integer id) {
        return service.getShoppingListById(id);
    }
    //

    // Add new ShoppingList Rest Api
    // http://localhost:8081/api/v1/shoppinglist/add
    @Operation(summary = "Crate new  ingredient on shopping list REST API", description = "Save new ingredient on shopping list on database")
    @ApiResponse(responseCode = "201", description = "HTTP Status 201 Created")
    @PostMapping("/add")
    public ResponseEntity<FormatResponse> add(@RequestBody ShoppingList s) {
         if (demoMode.isEnabled()) {
            throw new DemoModeException();
        }
        service.addIngredient(s);
        return new ResponseEntity<FormatResponse>(new FormatResponse("Ingredient added successfully!"), HttpStatus.CREATED);
    }
    //

    @PostMapping("/add-ingredients")
    public ResponseEntity<FormatResponse> addIngredients(@RequestBody List<ShoppingList> ingredients) {
        service.addIngredients(ingredients);
        return new ResponseEntity<>(new FormatResponse("Ingredients added successfully to shopping list!"), HttpStatus.CREATED);
    }

    // Update ShoppingList Rest Api
    // http://localhost:8081/api/v1/shoppinglist/1
    @Operation(summary = "Update ShoppingList REST API", description = "Update ShoppingList on database")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PatchMapping("/{id}")
    public ResponseEntity<FormatResponse> update(@PathVariable Integer id, @RequestBody ShoppingList updatedShoppingList) {
        if (demoMode.isEnabled()) {
            throw new DemoModeException();
        }
        if (!repository.existsById(id)) {
            return new ResponseEntity<>(new FormatResponse("ShoppingList not found!"), HttpStatus.NOT_FOUND);
        }
        return service.updateShoppingList(id, updatedShoppingList);
    }
    //

    // Delete ShoppingList Rest Api
    // http://localhost:8081/api/v1/shoppinglist/1
    @Operation(summary = "Delete ingredient from Shopping list REST API", description = "Delete ingredient from shopping list on database")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @DeleteMapping("/{id}")
    public ResponseEntity<FormatResponse> delete(@PathVariable Integer id) {
        if (demoMode.isEnabled()) {
            throw new DemoModeException();
        }
        service.deleteShoppingList(id);
        return new ResponseEntity<FormatResponse>(new FormatResponse("ShoppingList deleted successfully!"), HttpStatus.OK);
    }
    //

 
    // Get all ingredients Rest Api and obtain a stream data
    // http://localhost:8081/api/v1/shoppinglist/stream-test
    @Operation(summary = "Get all ingredients", description = "Retrieve a list of all ingredients in shopping list")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @GetMapping("/stream-test")
    public ResponseEntity<List<ShoppingList>> getAllShoppingLists() {
        List<ShoppingList> shoppingLists = service.getAllShoppingLists();
        List<ShoppingList> shoppingListsDto = shoppingLists.stream()
                .map(shoppingList -> modelMapper.map(shoppingList, ShoppingList.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(shoppingListsDto);
    }
    //

}
