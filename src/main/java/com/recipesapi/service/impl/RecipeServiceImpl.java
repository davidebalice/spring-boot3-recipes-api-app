package com.recipesapi.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.recipesapi.dto.IngredientDto;
import com.recipesapi.dto.RecipeDto;
import com.recipesapi.exception.ResourceNotFoundException;
import com.recipesapi.model.Category;
import com.recipesapi.model.Ingredient;
import com.recipesapi.model.Recipe;
import com.recipesapi.repository.CategoryRepository;
import com.recipesapi.repository.RecipeRepository;
import com.recipesapi.service.RecipeService;
import com.recipesapi.utility.FileUploadUtil;
import com.recipesapi.utility.FormatResponse;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository repository;
    private final CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${upload.path}")
    private String uploadPath;

    public RecipeServiceImpl(RecipeRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Recipe addRecipe(RecipeDto recipeDto) {
        Category category = categoryRepository.findById(recipeDto.getIdCategory())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Recipe recipe = new Recipe();
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setPreparationTime(recipeDto.getPreparationTime());
        recipe.setCookingTime(recipeDto.getCookingTime());
        recipe.setTips(recipeDto.getTips());
        recipe.setCategory(category);

        if (recipeDto.getIngredients() != null) {
            Set<Ingredient> ingredients = new HashSet<>();

            Map<String, Ingredient> existingIngredientMap = recipe.getIngredients().stream()
                    .collect(Collectors.toMap(Ingredient::getTitle, ingredient -> ingredient));

            for (IngredientDto ingredientDto : recipeDto.getIngredients()) {
                String ingredientTitle = ingredientDto.getTitle();
                int ingredientQuantity = ingredientDto.getQuantity();

                if (existingIngredientMap.containsKey(ingredientTitle)) {
                    // Update existing ingredient
                    Ingredient existingIngredient = existingIngredientMap.get(ingredientTitle);
                    existingIngredient.setQuantity(ingredientQuantity);
                    ingredients.add(existingIngredient);
                } else {
                    // Add new ingredient
                    Ingredient newIngredient = new Ingredient();
                    newIngredient.setTitle(ingredientTitle);
                    newIngredient.setQuantity(ingredientQuantity);
                    newIngredient.setRecipe(recipe);
                    ingredients.add(newIngredient);
                }
            }

            // Remove ingredients that are not in the updated list
            recipe.getIngredients().removeIf(ingredient -> !ingredients.contains(ingredient));

            // Set the updated ingredients to the recipe
            recipe.setIngredients(ingredients);
        }
        System.out.println(recipe.getIngredients());
        return repository.save(recipe);
    }

    @Override
    public Recipe addRecipeWithPhoto(RecipeDto recipeDto, MultipartFile imageFile) throws IOException {
        return saveRecipe(recipeDto, imageFile);
    }

    private Recipe saveRecipe(RecipeDto recipeDto, MultipartFile imageFile) throws IOException {
        Category category = categoryRepository.findById(recipeDto.getIdCategory())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Recipe recipe = modelMapper.map(recipeDto, Recipe.class);
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setPreparationTime(recipeDto.getPreparationTime());
        recipe.setCookingTime(recipeDto.getCookingTime());
        recipe.setTips(recipeDto.getTips());
        recipe.setCategory(category);

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = recipeDto.getTitle() + "_" + StringUtils.cleanPath(imageFile.getOriginalFilename());
            String uploadDir = uploadPath + "/image";
            FileUploadUtil.saveFile(uploadDir, fileName, imageFile);
            recipe.setImageUrl(fileName);
        }

        if (recipeDto.getIngredients() != null) {
            Set<Ingredient> ingredients = new HashSet<>();

            // Create a map of existing ingredients by title for easy lookup
            Map<String, Ingredient> existingIngredientMap = recipe.getIngredients().stream()
                    .collect(Collectors.toMap(Ingredient::getTitle, ingredient -> ingredient));

            for (IngredientDto ingredientDto : recipeDto.getIngredients()) {
                String ingredientTitle = ingredientDto.getTitle();
                int ingredientQuantity = ingredientDto.getQuantity();

                if (existingIngredientMap.containsKey(ingredientTitle)) {
                    // Update existing ingredient
                    Ingredient existingIngredient = existingIngredientMap.get(ingredientTitle);
                    existingIngredient.setQuantity(ingredientQuantity);
                    ingredients.add(existingIngredient);
                } else {
                    // Add new ingredient
                    Ingredient newIngredient = new Ingredient();
                    newIngredient.setTitle(ingredientTitle);
                    newIngredient.setQuantity(ingredientQuantity);
                    newIngredient.setRecipe(recipe);
                    ingredients.add(newIngredient);
                }
            }

            // Remove ingredients that are not in the updated list
            recipe.getIngredients().removeIf(ingredient -> !ingredients.contains(ingredient));

            // Set the updated ingredients to the recipe
            recipe.setIngredients(ingredients);
        }

        return repository.save(recipe);
    }

    @Override
    public Recipe getRecipeById(int recipeId) {
        return repository.findById(recipeId).orElseThrow(
                () -> new ResourceNotFoundException("Recipe", "id"));
    }

    @Override
    public ResponseEntity<FormatResponse> updateRecipe(int id, RecipeDto updatedRecipe) {
        try {
            if (!repository.existsById(id)) {
                return new ResponseEntity<FormatResponse>(new FormatResponse("Recipe not found"), HttpStatus.NOT_FOUND);
            }

            Recipe existingRecipe = repository.findById(id).get();

            if (updatedRecipe.getTitle() != null) {
                existingRecipe.setTitle(updatedRecipe.getTitle());
            }
            if (updatedRecipe.getDescription() != null) {
                existingRecipe.setDescription(updatedRecipe.getDescription());
            }
            if (updatedRecipe.getPreparationTime() != null) {
                existingRecipe.setPreparationTime(updatedRecipe.getPreparationTime());
            }
            if (updatedRecipe.getCookingTime() != null) {
                existingRecipe.setCookingTime(updatedRecipe.getCookingTime());
            }
            if (updatedRecipe.getTips() != null) {
                existingRecipe.setTips(updatedRecipe.getTips());
            }
          





            System.out.println(updatedRecipe.getImageUrl());

            if (updatedRecipe.getImageUrl() != null) {
                existingRecipe.setImageUrl(updatedRecipe.getImageUrl());
            }
            if (updatedRecipe.getIdCategory() >= 1) {
                Category category = categoryRepository.findById(updatedRecipe.getIdCategory())
                        .orElseThrow(() -> new RuntimeException("Category not found"));

                existingRecipe.setCategory(category);
            }

            if (updatedRecipe.getIngredients() != null) {
                Set<Ingredient> existingIngredients = existingRecipe.getIngredients();

                Map<String, Ingredient> existingIngredientMap = existingIngredients.stream()
                        .collect(Collectors.toMap(Ingredient::getTitle, ingredient -> ingredient));

                for (IngredientDto updatedIngredientDto : updatedRecipe.getIngredients()) {
                    String ingredientTitle = updatedIngredientDto.getTitle();
                    int ingredientQuantity = updatedIngredientDto.getQuantity();

                    if (existingIngredientMap.containsKey(ingredientTitle)) {
                        Ingredient existingIngredient = existingIngredientMap.get(ingredientTitle);
                        existingIngredient.setQuantity(ingredientQuantity);
                    } else {
                        Ingredient newIngredient = new Ingredient();
                        newIngredient.setTitle(ingredientTitle);
                        newIngredient.setQuantity(ingredientQuantity);
                        newIngredient.setRecipe(existingRecipe);
                        existingIngredients.add(newIngredient);
                    }
                }

                existingIngredients.removeIf(ingredient -> updatedRecipe.getIngredients().stream()
                        .noneMatch(
                                updatedIngredientDto -> updatedIngredientDto.getTitle().equals(ingredient.getTitle())));

                existingRecipe.setIngredients(existingIngredients);

            }

            repository.save(existingRecipe);

            return new ResponseEntity<FormatResponse>(new FormatResponse("Recipe updated successfully!"),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<FormatResponse>(new FormatResponse("Error updating recipe"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<FormatResponse> deleteRecipe(Integer recipeId) {
        Optional<Recipe> pOptional = repository.findById(recipeId);
        if (pOptional.isPresent()) {
            Recipe p = pOptional.get();
            repository.delete(p);
            return new ResponseEntity<FormatResponse>(new FormatResponse("Recipe deleted successfully"), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Recipe", "id");
        }
    }

    @Override
    public List<Recipe> searchRecipes(String keyword) {
        return repository.searchRecipes(keyword);
    }

    @Override
    public List<Recipe> searchRecipesByCategoryId(int categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return repository.findAll();
    }

    @Override
    public String uploadImage(int id, MultipartFile multipartFile, String uploadPath) throws IOException {


        if (multipartFile == null || multipartFile.getOriginalFilename() == null) {
            throw new IllegalArgumentException("Invalid file");
        }

        String fileName = id + "_" + StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = uploadPath + "/image";
        Path filePath = Paths.get(uploadDir, fileName);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        Recipe recipe = getRecipeById(id);
        RecipeDto recipeDto = modelMapper.map(recipe, RecipeDto.class);
        recipeDto.setImageUrl(fileName);
        updateRecipe(id, recipeDto);

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/recipes/image/")
                .path(fileName)
                .toUriString();
    }

    @Override
    public ResponseEntity<Resource> downloadImage(String fileName, HttpServletRequest request, String uploadPath)
            throws IOException {
        Path filePath = Paths.get(uploadPath + "/image").resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
