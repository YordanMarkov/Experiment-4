package org.example.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MealDbClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL =
            "https://www.themealdb.com/api/json/v1/1";

    public String getRandomMealRaw() {
        String url = BASE_URL + "/random.php";
        return restTemplate.getForObject(url, String.class);
    }
}