package org.example.service;

import org.example.dto.RecipeResponse;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    public RecipeResponse getSampleRecipe() {
        return new RecipeResponse(
                "1",
                "Chicken Pasta",
                "Pasta",
                "Italian",
                "Boil pasta, cook chicken, and mix together."
        );
    }
}