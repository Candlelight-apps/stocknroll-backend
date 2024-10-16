package com.candlelightapps.stocknroll_backend.service.SpoonacularApi;

import com.candlelightapps.stocknroll_backend.model.spoonacular.Data;
import com.candlelightapps.stocknroll_backend.model.spoonacular.ingredient.DataIngredient;
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
        cuisine = cuisine==null? "":cuisine;
        diet = diet==null? "":diet;
        intolerances = intolerances==null? "":intolerances;

        Data result = client.get()
                .uri("/recipes/complexSearch?apiKey=" + ApiConfig.getApiKey() + "&cuisine=" + cuisine + "&diet=" + diet + "&intolerances=" + intolerances + "&addRecipeInformation=true")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(Data.class).block();
        return result;
    }
    public static Data getRecipesByIngredients(String ingredients) {
        ingredients = ingredients==null? "":ingredients;
        Data result = client.get()
                .uri("/recipes/complexSearch?apiKey=" + ApiConfig.getApiKey() + "&includeIngredients=" + ingredients + "&addRecipeInformation=true")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(Data.class).block();
        return result;
    }
    public static DataIngredient getIngredientByName(String ingredient) {
        if(ingredient==null || ingredient.isEmpty() || ingredient.isBlank()) return null;
        DataIngredient result = client.get()
                .uri("/food/ingredients/search?apiKey=" + ApiConfig.getApiKey() + "&query=" + ingredient + "&number=1")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(DataIngredient.class).block();
        return result;
    }

}
