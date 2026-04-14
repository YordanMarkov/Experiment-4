package org.example.service;

import org.example.dto.RecipeResponse;
import org.example.dto.RecipeSummaryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

import org.example.client.MealDbClient;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class RecipeService {

    private final MealDbClient mealDbClient;

    public RecipeService(MealDbClient mealDbClient) {
        this.mealDbClient = mealDbClient;
    }

    public RecipeResponse getSampleRecipe() {
        return new RecipeResponse(
                "1",
                "Chicken Pasta",
                "Pasta",
                "Italian",
                "Boil pasta, cook chicken, and mix together."
        );
    }

    public RecipeResponse getRecipeById(String id) {
        if ("1".equals(id)) {
            return new RecipeResponse(
                    "1",
                    "Chicken Pasta",
                    "Pasta",
                    "Italian",
                    "Boil pasta, cook chicken, and mix together."
            );
        }

        if ("2".equals(id)) {
            return new RecipeResponse(
                    "2",
                    "Tomato Soup",
                    "Soup",
                    "International",
                    "Boil tomatoes, blend them, and serve warm."
            );
        }

        return new RecipeResponse(
                "0",
                "Unknown Recipe",
                "Unknown",
                "Unknown",
                "No instructions available."
        );
    }

    public RecipeResponse getRandomRecipe() {
        JsonNode root = mealDbClient.getRandomMeal();
        JsonNode meal = root.get("meals").get(0);

        return new RecipeResponse(
                meal.get("idMeal").asText(),
                meal.get("strMeal").asText(),
                meal.get("strCategory").asText(),
                meal.get("strArea").asText(),
                meal.get("strInstructions").asText()
        );
    }

    public List<RecipeSummaryResponse> searchRecipesByName(String name) {
        return List.of(
                new RecipeSummaryResponse("1", "Chicken Pasta"),
                new RecipeSummaryResponse("2", "Tomato Soup")
        );
    }

    public List<RecipeSummaryResponse> getRecipesByCategory(String category) {
        return List.of(
                new RecipeSummaryResponse("3", category + " Special"),
                new RecipeSummaryResponse("4", category + " Delight")
        );
    }

    public List<RecipeSummaryResponse> getRecipesByArea(String area) {
        return List.of(
                new RecipeSummaryResponse("5", area + " Chicken Dish"),
                new RecipeSummaryResponse("6", area + " Rice Bowl")
        );
    }

    public RecipeResponse recommendRecipeByIngredient(String ingredient) {
        return new RecipeResponse(
                "7",
                ingredient + " Recipe",
                "Custom",
                "International",
                "Use " + ingredient + " and cook a simple meal."
        );
    }
}