package org.example.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MealDbClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL =
            "https://www.themealdb.com/api/json/v1/1";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode getRandomMeal() {
        String url = BASE_URL + "/random.php";
        String response = restTemplate.getForObject(url, String.class);

        try {
            return objectMapper.readTree(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }

    public JsonNode getMealsByIngredient(String ingredient) {
        String url = BASE_URL + "/filter.php?i=" + ingredient;
        String response = restTemplate.getForObject(url, String.class);

        try {
            return objectMapper.readTree(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }

    public JsonNode getMealById(String id) {
        String url = BASE_URL + "/lookup.php?i=" + id;
        String response = restTemplate.getForObject(url, String.class);

        try {
            return objectMapper.readTree(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }
}