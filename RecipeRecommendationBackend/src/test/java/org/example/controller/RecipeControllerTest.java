package org.example.controller;

import org.example.dto.RecipeResponse;
import org.example.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
@Import(RecipeControllerTest.TestConfig.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 👇 Provide a fake implementation instead of Mockito
    static class TestConfig {
        @Bean
        public RecipeService recipeService() {
            return new RecipeService(null) {
                @Override
                public RecipeResponse getRecipeById(String id) {
                    return new RecipeResponse(
                            "52772",
                            "Teriyaki Chicken Casserole",
                            "Chicken",
                            "Japanese",
                            "Bake and serve."
                    );
                }
            };
        }
    }

    @Test
    void getRecipeById_shouldReturnRecipeJson() throws Exception {
        mockMvc.perform(get("/api/recipes/52772"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("52772"))
                .andExpect(jsonPath("$.name").value("Teriyaki Chicken Casserole"))
                .andExpect(jsonPath("$.category").value("Chicken"))
                .andExpect(jsonPath("$.area").value("Japanese"))
                .andExpect(jsonPath("$.instructions").value("Bake and serve."));
    }
}