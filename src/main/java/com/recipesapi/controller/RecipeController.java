package com.recipesapi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.recipesapi.dto.RecipeDto;
import com.recipesapi.model.Recipe;
import com.recipesapi.repository.RecipeRepository;
import com.recipesapi.service.RecipeService;
import com.recipesapi.utility.FormatResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Tag(name = "CRUD REST APIs for Recipe Resource", description = "RECIPES CRUD REST APIs - Create Recipe, Update Recipe, Get Recipe, Get All Recipes, Delete Recipe")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/recipes/")
public class RecipeController {

    private final RecipeRepository repository;
    private final RecipeService service;

    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);

    @Autowired
    private ModelMapper modelMapper;

    @Value("${upload.path}")
    private String uploadPath;

    public RecipeController(RecipeRepository repository, RecipeService service) {
        this.repository = repository;
        this.service = service;
    }

    // Get all Recipes Rest Api
    // http://localhost:8081/api/v1/recipes
    @Operation(summary = "Get all recipes", description = "Retrieve a list of all recipes")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @GetMapping("/")
    public ResponseEntity<Iterable<RecipeDto>> list() {
        Iterable<Recipe> recipes = repository.findAll();
        List<RecipeDto> recipesDto = new ArrayList<>();
        for (Recipe recipe : recipes) {
            RecipeDto recipeDto = modelMapper.map(recipe, RecipeDto.class);
            recipesDto.add(recipeDto);
        }
        return ResponseEntity.ok(recipesDto);
    }
    //

    // Get single Recipe Rest Api by Id (get id by url)
    // http://localhost:8081/api/v1/recipes/1
    @Operation(summary = "Get Recipe By ID REST API", description = "Get Recipe By ID REST API is used to get a single Recipe from the database, get id by url")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getById(@PathVariable Integer id) {
        Recipe recipe = service.getRecipeById(id);
        System.out.println(recipe);
        if (recipe != null) {
            RecipeDto recipeDto = modelMapper.map(recipe, RecipeDto.class);
            return new ResponseEntity<>(recipeDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //

    // Get single Recipe Rest Api (get id by querystring)
    // http://localhost:8081/api/v1/recipe?id=1
    @Operation(summary = "Get Recipe By ID REST API", description = "Get Recipe By ID REST API is used to get a single Recipe from the database, get id by querystring")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @GetMapping("/recipe")
    public Recipe getByIdQs(@RequestParam Integer id) {
        return service.getRecipeById(id);
    }
    //

    // Add new Recipe Rest Api
    // http://localhost:8081/api/v1/recipes/add
    @Operation(summary = "Crate new  Recipe REST API", description = "Save new Recipe on database")
    @ApiResponse(responseCode = "201", description = "HTTP Status 201 Created")
    @PostMapping("/add")
    public ResponseEntity<FormatResponse> addRecipe(@Valid @RequestBody RecipeDto recipeDto) {
System.out.println(recipeDto);
        try {
            service.addRecipe(recipeDto);
            return new ResponseEntity<>(new FormatResponse("Recipe added successfully!"), HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(new FormatResponse("Error uploading image"), HttpStatus.BAD_REQUEST);
        }
    }

    // Add new Recipe Rest Api - Recipe + photo
    // http://localhost:8081/api/v1/recipes/add2
    @Operation(summary = "Crate new  Recipe + photo REST API", description = "Save new Recipe on database with photo")
    @ApiResponse(responseCode = "201", description = "HTTP Status 201 Created")
    @PostMapping(value = "/add-with-photo", consumes = "multipart/form-data")
    // @RequestBody RecipeDto recipeDto,
    public ResponseEntity<FormatResponse> addRecipeWithPhoto(
            // @ModelAttribute RecipeDto recipeDto,
            // @RequestPart("recipeDto") RecipeDto recipeDto,
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart("idCategory") Long idCategory,
            @RequestPart("ingredients") List<Map<String, String>> ingredients,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        try {
            RecipeDto recipeDto = new RecipeDto();
            recipeDto.setTitle(title);
            recipeDto.setDescription(description);
            service.addRecipeWithPhoto(recipeDto, imageFile);
            return new ResponseEntity<>(new FormatResponse("Recipe added successfully!"), HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(new FormatResponse("Error uploading image"), HttpStatus.BAD_REQUEST);
        }
    }

    // Update Recipe Rest Api
    // http://localhost:8081/api/v1/recipes/1
    @Operation(summary = "Update Recipe REST API", description = "Update Recipe on database")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PatchMapping("/{id}")
    public ResponseEntity<FormatResponse> update(@PathVariable Integer id, @RequestBody RecipeDto updatedRecipe) {
        if (!repository.existsById(id)) {
            return new ResponseEntity<>(new FormatResponse("Recipe not found!"), HttpStatus.NOT_FOUND);
        }
        return service.updateRecipe(id, updatedRecipe);
    }
    //

    // Delete Recipe Rest Api
    // http://localhost:8081/api/v1/recipes/1
    @Operation(summary = "Delete Recipe REST API", description = "Delete Recipe on database")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @DeleteMapping("/{id}")
    public ResponseEntity<FormatResponse> delete(@PathVariable Integer id) {
        service.deleteRecipe(id);
        return new ResponseEntity<FormatResponse>(new FormatResponse("Recipe deleted successfully!"), HttpStatus.OK);
    }
    //

    // Search Recipe Rest Api
    // http://localhost:8081/api/v1/recipes/search
    @Operation(summary = "Search Recipe REST API", description = "Search Recipe on database by filter")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @GetMapping("/search")
    public ResponseEntity<List<RecipeDto>> searchRecipes(@RequestParam("keyword") String keyword) {
        List<Recipe> recipes = service.searchRecipes(keyword);
        List<RecipeDto> recipesDto = recipes.stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(recipesDto);
    }
    //

    // Search Recipe by Category Rest Api
    // http://localhost:8081/api/v1/recipes/searchByCategoryId
    @Operation(summary = "Search Recipe by Category Api REST API", description = "Search Recipe by Category Api on database by id")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @GetMapping("/searchByCategoryId")
    public ResponseEntity<List<RecipeDto>> searchRecipesByCategoryId(@RequestParam int categoryId) {
        List<Recipe> recipes = service.searchRecipesByCategoryId(categoryId);

        List<RecipeDto> recipesDto = recipes.stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(recipesDto);
    }
    //

    // Get all recipes Rest Api and obtain a stream data
    // http://localhost:8081/api/v1/recipes/stream-test
    @Operation(summary = "Get all recipes", description = "Retrieve a list of all recipes")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @GetMapping("/stream-test")
    public ResponseEntity<List<RecipeDto>> getAllRecipes() {
        List<Recipe> recipes = service.getAllRecipes();
        List<RecipeDto> recipesDto = recipes.stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(recipesDto);
    }
    //

    @Operation(summary = "Upload recipe image", description = "Upload photo of recipe")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PostMapping("/{id}/uploadImage")
    public ResponseEntity<FormatResponse> uploadImage(@PathVariable int id,
            @RequestParam("image") MultipartFile multipartFile) {
        try {
            String fileDownloadUri = service.uploadImage(id, multipartFile, uploadPath);
            return new ResponseEntity<>(new FormatResponse(fileDownloadUri), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(new FormatResponse("Error uploading image"), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Download recipe image", description = "Download photo of recipe")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @GetMapping("/image/{fileName:.+}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String fileName, HttpServletRequest request) {
        try {
            return service.downloadImage(fileName, request, uploadPath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
