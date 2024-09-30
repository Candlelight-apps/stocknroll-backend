package com.candlelightapps.stocknroll_backend.service.SpoonacularApi;


import com.candlelightapps.stocknroll_backend.model.spoonacular.recipeByCriteria.Data;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;


public class SpoonacularDAO {
    private static final String BASE_URL = "https://api.spoonacular.com";
    private static final WebClient client = WebClient.builder().baseUrl(BASE_URL).build();
    static {
        try {
            ApiConfig apiConfig = new ApiConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Data getRecipesByCriteria(String cuisine, String diet, String intolerances) {

        Data result = client.get()
                .uri("/recipes/complexSearch?apiKey=" + ApiConfig.getApiKey() + "&cuisine=" + cuisine + "&diet=" + diet + "&intolerances=" + intolerances)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(Data.class).block();
        return result;
    }
}
