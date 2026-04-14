package org.example.controller;

import org.example.dto.RecipeResponse;
import org.example.service.RecipeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/api/recipes/sample")
    public RecipeResponse getSampleRecipe() {
        return recipeService.getSampleRecipe();
    }
}