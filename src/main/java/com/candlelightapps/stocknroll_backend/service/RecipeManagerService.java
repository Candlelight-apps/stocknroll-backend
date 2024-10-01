package com.candlelightapps.stocknroll_backend.service;

import com.candlelightapps.stocknroll_backend.model.Recipe;
import com.candlelightapps.stocknroll_backend.model.spoonacular.recipeByCriteria.Result;

import java.util.ArrayList;

public interface RecipeManagerService {

    Recipe addRecipe(Recipe recipe);

    ArrayList<Result> getRecipeByCriteria(String cuisine, String diet, String intolerance);
}
