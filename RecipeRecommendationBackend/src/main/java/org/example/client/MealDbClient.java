package org.example.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MealDbClient implements MealDb {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl;

    public MealDbClient(@Value("${mealdb.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public JsonNode getRandomMeal() {
        return getJson(baseUrl + "/random.php");
    }

    @Override
    public JsonNode getMealsByIngredient(String ingredient) {
        return getJson(baseUrl + "/filter.php?i=" + ingredient);
    }

    @Override
    public JsonNode getMealById(String id) {
        return getJson(baseUrl + "/lookup.php?i=" + id);
    }

    @Override
    public JsonNode searchMealsByName(String name) {
        return getJson(baseUrl + "/search.php?s=" + name);
    }

    @Override
    public JsonNode getMealsByCategory(String category) {
        return getJson(baseUrl + "/filter.php?c=" + category);
    }

    @Override
    public JsonNode getMealsByArea(String area) {
        return getJson(baseUrl + "/filter.php?a=" + area);
    }

    private JsonNode getJson(String url) {
        String response = restTemplate.getForObject(url, String.class);

        try {
            return objectMapper.readTree(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }
}