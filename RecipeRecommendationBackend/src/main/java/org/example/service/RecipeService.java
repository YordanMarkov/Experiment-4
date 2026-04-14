package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.client.MealDbClient;
import org.example.dto.RecipeResponse;
import org.example.dto.RecipeSummaryResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService {

    private final MealDbClient mealDbClient;

    public RecipeService(MealDbClient mealDbClient) {
        this.mealDbClient = mealDbClient;
    }

    public RecipeResponse getRecipeById(String id) {
        JsonNode root = mealDbClient.getMealById(id);
        JsonNode meals = root.get("meals");

        if (meals == null || meals.isEmpty()) {
            return new RecipeResponse(
                    "0",
                    "No recipe found",
                    "Unknown",
                    "Unknown",
                    "No instructions available."
            );
        }

        JsonNode meal = meals.get(0);

        return new RecipeResponse(
                meal.get("idMeal").asText(),
                meal.get("strMeal").asText(),
                meal.get("strCategory").asText(),
                meal.get("strArea").asText(),
                meal.get("strInstructions").asText()
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
        JsonNode root = mealDbClient.searchMealsByName(name);
        JsonNode meals = root.get("meals");

        if (meals == null || meals.isEmpty()) {
            return List.of();
        }

        List<RecipeSummaryResponse> results = new ArrayList<>();

        for (JsonNode meal : meals) {
            results.add(new RecipeSummaryResponse(
                    meal.get("idMeal").asText(),
                    meal.get("strMeal").asText()
            ));
        }

        return results;
    }

    public List<RecipeSummaryResponse> getRecipesByCategory(String category) {
        JsonNode root = mealDbClient.getMealsByCategory(category);
        JsonNode meals = root.get("meals");

        if (meals == null || meals.isEmpty()) {
            return List.of();
        }

        List<RecipeSummaryResponse> results = new ArrayList<>();

        for (JsonNode meal : meals) {
            results.add(new RecipeSummaryResponse(
                    meal.get("idMeal").asText(),
                    meal.get("strMeal").asText()
            ));
        }

        return results;
    }

    public List<RecipeSummaryResponse> getRecipesByArea(String area) {
        JsonNode root = mealDbClient.getMealsByArea(area);
        JsonNode meals = root.get("meals");

        if (meals == null || meals.isEmpty()) {
            return List.of();
        }

        List<RecipeSummaryResponse> results = new ArrayList<>();

        for (JsonNode meal : meals) {
            results.add(new RecipeSummaryResponse(
                    meal.get("idMeal").asText(),
                    meal.get("strMeal").asText()
            ));
        }

        return results;
    }

    public RecipeResponse recommendRecipeByIngredient(String ingredient) {
        JsonNode filterResponse = mealDbClient.getMealsByIngredient(ingredient);
        JsonNode meals = filterResponse.get("meals");

        if (meals == null || meals.isEmpty()) {
            return new RecipeResponse(
                    "0",
                    "No recipe found",
                    "Unknown",
                    "Unknown",
                    "No instructions available."
            );
        }

        JsonNode firstMeal = meals.get(0);
        String mealId = firstMeal.get("idMeal").asText();

        JsonNode lookupResponse = mealDbClient.getMealById(mealId);
        JsonNode meal = lookupResponse.get("meals").get(0);

        return new RecipeResponse(
                meal.get("idMeal").asText(),
                meal.get("strMeal").asText(),
                meal.get("strCategory").asText(),
                meal.get("strArea").asText(),
                meal.get("strInstructions").asText()
        );
    }
}