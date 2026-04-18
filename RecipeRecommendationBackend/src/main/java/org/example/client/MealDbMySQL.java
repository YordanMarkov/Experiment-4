package org.example.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

//@Component
public class MealDbMySQL implements MealDb {

    @Override
    public JsonNode getRandomMeal() {
        return null;
    }

    @Override
    public JsonNode getMealsByIngredient(String ingredient) {
        return null;
    }

    @Override
    public JsonNode getMealById(String id) {
        return null;
    }

    @Override
    public JsonNode searchMealsByName(String name) {
        return null;
    }

    @Override
    public JsonNode getMealsByCategory(String category) {
        return null;
    }

    @Override
    public JsonNode getMealsByArea(String area) {
        return null;
    }
}
