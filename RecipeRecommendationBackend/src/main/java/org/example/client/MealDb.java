package org.example.client;

import com.fasterxml.jackson.databind.JsonNode;

public interface MealDb {
    JsonNode getRandomMeal();

    JsonNode getMealsByIngredient(String ingredient);

    JsonNode getMealById(String id);

    JsonNode searchMealsByName(String name);

    JsonNode getMealsByCategory(String category);

    JsonNode getMealsByArea(String area);
}
