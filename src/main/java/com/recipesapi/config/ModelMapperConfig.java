package com.recipesapi.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.recipesapi.dto.RecipeDto;
import com.recipesapi.model.Recipe;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<Recipe, RecipeDto> typeMap = modelMapper.createTypeMap(Recipe.class, RecipeDto.class);
        typeMap.addMapping(Recipe::getIngredients, RecipeDto::setIngredients);
        typeMap.addMappings(mapper -> mapper.map(src -> src.getCategory(), RecipeDto::setCategoryDto));

        return modelMapper;
    }
}
