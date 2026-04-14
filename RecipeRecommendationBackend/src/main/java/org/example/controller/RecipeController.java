package org.example.controller;

import org.example.dto.RecipeResponse;
import org.example.dto.RecipeSummaryResponse;
import org.example.service.RecipeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/sample")
    public RecipeResponse getSampleRecipe() {
        return recipeService.getSampleRecipe();
    }

    @GetMapping("/{id}")
    public RecipeResponse getRecipeById(@PathVariable String id) {
        return recipeService.getRecipeById(id);
    }

    @GetMapping("/random")
    public String getRandomRecipe() {
        return recipeService.getRandomRecipe();
    }

    @GetMapping("/search")
    public List<RecipeSummaryResponse> searchRecipesByName(@RequestParam String name) {
        return recipeService.searchRecipesByName(name);
    }

    @GetMapping("/category")
    public List<RecipeSummaryResponse> getRecipesByCategory(@RequestParam String name) {
        return recipeService.getRecipesByCategory(name);
    }

    @GetMapping("/area")
    public List<RecipeSummaryResponse> getRecipesByArea(@RequestParam String name) {
        return recipeService.getRecipesByArea(name);
    }

    @GetMapping("/recommend")
    public RecipeResponse recommendRecipeByIngredient(@RequestParam String ingredient) {
        return recipeService.recommendRecipeByIngredient(ingredient);
    }
}