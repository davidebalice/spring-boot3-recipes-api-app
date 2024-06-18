package com.recipesapi.dto;

import java.util.Set;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecipeDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private int idCategory;
    private String imageUrl;
    private CategoryDto categoryDto;
    // @JsonDeserialize(using = IngredientDtoSetDeserializer.class)
    private Set<IngredientDto> ingredients;

}