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
        // typeMap.addMapping(src -> src.getCategory().getId(),
        // RecipeDto::setIdCategory);
        //typeMap.addMapping(src -> src.getCategory().getId(), RecipeDto::setIdCategory);
       // typeMap.addMapping(src -> src.getCategory().getName(), RecipeDto::setCategoryDtoName);

        return modelMapper;
    }
}
/*
 * modelMapper.createTypeMap(Recipe.class, RecipeDto.class)
 * .addMapping(src -> src.getCategory().getId(), RecipeDto::setCategoryDtoId)
 * .addMapping(src -> src.getCategory().getName(),
 * RecipeDto::setCategoryDtoTitle);
 * 
 * 
 */