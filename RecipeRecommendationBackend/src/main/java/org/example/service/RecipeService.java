package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.example.client.MealDb;
import org.example.dto.RecipeResponse;
import org.example.dto.RecipeSummaryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService {

    private static final Logger log = LoggerFactory.getLogger(RecipeService.class);

    private final MealDb mealDb;

    public RecipeService(MealDb mealDb) {
        this.mealDb = mealDb;
    }

    public RecipeResponse getRecipeById(String id) {
        JsonNode root = mealDb.getMealById(id);
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

    @Retry(name = "mealDbRetry", fallbackMethod = "randomRecipeFallback")
    @CircuitBreaker(name = "mealDbCircuitBreaker", fallbackMethod = "randomRecipeFallback")
    public RecipeResponse getRandomRecipe() {
        log.info("Requesting random recipe from external MealDB service");
        JsonNode root = mealDb.getRandomMeal();
        JsonNode meals = root.get("meals");

        if (meals == null || meals.isEmpty()) {
            throw new RuntimeException("MealDB returned no meals for random recipe");
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

    public RecipeResponse randomRecipeFallback(Throwable t) {
        log.warn("Fallback triggered for getRandomRecipe: {}", t.toString());

        return new RecipeResponse(
                "fallback-1",
                "Fallback Recipe",
                "Unavailable",
                "Unknown",
                "A fallback response was returned because the external recipe service failed."
        );
    }

    public List<RecipeSummaryResponse> searchRecipesByName(String name) {
        JsonNode root = mealDb.searchMealsByName(name);
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
        JsonNode root = mealDb.getMealsByCategory(category);
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
        JsonNode root = mealDb.getMealsByArea(area);
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
        JsonNode filterResponse = mealDb.getMealsByIngredient(ingredient);
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

        JsonNode lookupResponse = mealDb.getMealById(mealId);
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