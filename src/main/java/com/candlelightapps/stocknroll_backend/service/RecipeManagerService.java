package com.candlelightapps.stocknroll_backend.service;

import com.candlelightapps.stocknroll_backend.model.Recipe;
import com.candlelightapps.stocknroll_backend.model.spoonacular.Result;

import java.util.ArrayList;
import java.util.List;

public interface RecipeManagerService {

    Recipe addRecipe(Recipe recipe);

    ArrayList<Result> getRecipeByCriteria(String cuisine, String diet, String intolerance);


    String deleteRecipeById(Long recipeId);

    List<Recipe> getAllRecipes();

    ArrayList<Result> getRecipesByIngredient(ArrayList<String> ingredients);
}
