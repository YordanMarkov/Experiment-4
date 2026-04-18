package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.client.MealDb;
import org.example.dto.RecipeResponse;
import org.example.dto.RecipeSummaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private MealDb mealDb;

    private RecipeService recipeService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        recipeService = new RecipeService(mealDb);
        objectMapper = new ObjectMapper();
    }

    private JsonNode json(String value) throws Exception {
        return objectMapper.readTree(value);
    }

    @Test
    void getRecipeById_shouldReturnRecipe_whenMealExists() throws Exception {
        String id = "52772";

        JsonNode response = json("""
            {
              "meals": [
                {
                  "idMeal": "52772",
                  "strMeal": "Teriyaki Chicken Casserole",
                  "strCategory": "Chicken",
                  "strArea": "Japanese",
                  "strInstructions": "Bake and serve."
                }
              ]
            }
        """);

        when(mealDb.getMealById(id)).thenReturn(response);

        RecipeResponse result = recipeService.getRecipeById(id);

        assertEquals("52772", result.getId());
        assertEquals("Teriyaki Chicken Casserole", result.getName());
        assertEquals("Chicken", result.getCategory());
        assertEquals("Japanese", result.getArea());
        assertEquals("Bake and serve.", result.getInstructions());

        verify(mealDb).getMealById(id);
    }

    @Test
    void getRecipeById_shouldReturnFallback_whenMealsIsNull() throws Exception {
        String id = "99999";

        JsonNode response = json("""
            {
              "meals": null
            }
        """);

        when(mealDb.getMealById(id)).thenReturn(response);

        RecipeResponse result = recipeService.getRecipeById(id);

        assertEquals("0", result.getId());
        assertEquals("No recipe found", result.getName());
        assertEquals("Unknown", result.getCategory());
        assertEquals("Unknown", result.getArea());
        assertEquals("No instructions available.", result.getInstructions());

        verify(mealDb).getMealById(id);
    }

    @Test
    void getRandomRecipe_shouldReturnRecipe() throws Exception {
        JsonNode response = json("""
            {
              "meals": [
                {
                  "idMeal": "12345",
                  "strMeal": "Random Pasta",
                  "strCategory": "Pasta",
                  "strArea": "Italian",
                  "strInstructions": "Cook pasta."
                }
              ]
            }
        """);

        when(mealDb.getRandomMeal()).thenReturn(response);

        RecipeResponse result = recipeService.getRandomRecipe();

        assertEquals("12345", result.getId());
        assertEquals("Random Pasta", result.getName());
        assertEquals("Pasta", result.getCategory());
        assertEquals("Italian", result.getArea());
        assertEquals("Cook pasta.", result.getInstructions());

        verify(mealDb).getRandomMeal();
    }

    @Test
    void searchRecipesByName_shouldReturnMappedResults_whenMealsExist() throws Exception {
        JsonNode response = json("""
            {
              "meals": [
                { "idMeal": "1", "strMeal": "Arrabiata" },
                { "idMeal": "2", "strMeal": "Carbonara" }
              ]
            }
        """);

        when(mealDb.searchMealsByName("pasta")).thenReturn(response);

        List<RecipeSummaryResponse> result = recipeService.searchRecipesByName("pasta");

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Arrabiata", result.get(0).getName());
        assertEquals("2", result.get(1).getId());
        assertEquals("Carbonara", result.get(1).getName());

        verify(mealDb).searchMealsByName("pasta");
    }

    @Test
    void searchRecipesByName_shouldReturnEmptyList_whenMealsIsNull() throws Exception {
        JsonNode response = json("""
            {
              "meals": null
            }
        """);

        when(mealDb.searchMealsByName("unknown")).thenReturn(response);

        List<RecipeSummaryResponse> result = recipeService.searchRecipesByName("unknown");

        assertTrue(result.isEmpty());

        verify(mealDb).searchMealsByName("unknown");
    }

    @Test
    void getRecipesByCategory_shouldReturnMappedResults() throws Exception {
        JsonNode response = json("""
            {
              "meals": [
                { "idMeal": "10", "strMeal": "Chicken Handi" }
              ]
            }
        """);

        when(mealDb.getMealsByCategory("Chicken")).thenReturn(response);

        List<RecipeSummaryResponse> result = recipeService.getRecipesByCategory("Chicken");

        assertEquals(1, result.size());
        assertEquals("10", result.get(0).getId());
        assertEquals("Chicken Handi", result.get(0).getName());

        verify(mealDb).getMealsByCategory("Chicken");
    }

    @Test
    void getRecipesByArea_shouldReturnMappedResults() throws Exception {
        JsonNode response = json("""
            {
              "meals": [
                { "idMeal": "20", "strMeal": "Sushi" }
              ]
            }
        """);

        when(mealDb.getMealsByArea("Japanese")).thenReturn(response);

        List<RecipeSummaryResponse> result = recipeService.getRecipesByArea("Japanese");

        assertEquals(1, result.size());
        assertEquals("20", result.get(0).getId());
        assertEquals("Sushi", result.get(0).getName());

        verify(mealDb).getMealsByArea("Japanese");
    }

    @Test
    void recommendRecipeByIngredient_shouldReturnFallback_whenNoMealsFound() throws Exception {
        JsonNode filterResponse = json("""
            {
              "meals": null
            }
        """);

        when(mealDb.getMealsByIngredient("dragonfruit")).thenReturn(filterResponse);

        RecipeResponse result = recipeService.recommendRecipeByIngredient("dragonfruit");

        assertEquals("0", result.getId());
        assertEquals("No recipe found", result.getName());
        assertEquals("Unknown", result.getCategory());
        assertEquals("Unknown", result.getArea());
        assertEquals("No instructions available.", result.getInstructions());

        verify(mealDb).getMealsByIngredient("dragonfruit");
        verify(mealDb, never()).getMealById(anyString());
    }

    @Test
    void recommendRecipeByIngredient_shouldLookupFullRecipe_whenIngredientMatchExists() throws Exception {
        JsonNode filterResponse = json("""
            {
              "meals": [
                { "idMeal": "52772", "strMeal": "Teriyaki Chicken Casserole" }
              ]
            }
        """);

        JsonNode lookupResponse = json("""
            {
              "meals": [
                {
                  "idMeal": "52772",
                  "strMeal": "Teriyaki Chicken Casserole",
                  "strCategory": "Chicken",
                  "strArea": "Japanese",
                  "strInstructions": "Bake and serve."
                }
              ]
            }
        """);

        when(mealDb.getMealsByIngredient("chicken")).thenReturn(filterResponse);
        when(mealDb.getMealById("52772")).thenReturn(lookupResponse);

        RecipeResponse result = recipeService.recommendRecipeByIngredient("chicken");

        assertEquals("52772", result.getId());
        assertEquals("Teriyaki Chicken Casserole", result.getName());
        assertEquals("Chicken", result.getCategory());
        assertEquals("Japanese", result.getArea());
        assertEquals("Bake and serve.", result.getInstructions());

        verify(mealDb).getMealsByIngredient("chicken");
        verify(mealDb).getMealById("52772");
    }
}