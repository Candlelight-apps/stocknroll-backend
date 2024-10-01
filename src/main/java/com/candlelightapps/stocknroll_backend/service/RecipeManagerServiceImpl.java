package com.candlelightapps.stocknroll_backend.service;

import com.candlelightapps.stocknroll_backend.exception.ItemNotFoundException;
import com.candlelightapps.stocknroll_backend.model.Recipe;
import com.candlelightapps.stocknroll_backend.model.spoonacular.recipeByCriteria.Data;
import com.candlelightapps.stocknroll_backend.model.spoonacular.recipeByCriteria.Result;
import com.candlelightapps.stocknroll_backend.repository.RecipeManagerRepository;
import com.candlelightapps.stocknroll_backend.service.SpoonacularApi.SpoonacularDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RecipeManagerServiceImpl implements RecipeManagerService {

    @Autowired
    RecipeManagerRepository recipeManagerRepository;

    @Override
    public Recipe addRecipe(Recipe recipe) {
        return recipeManagerRepository.save(recipe);
    }

    @Override
    public ArrayList<Result> getRecipeByCriteria(String cuisine, String diet, String intolerance) {
        Data recipeData  = SpoonacularDAO.getRecipesByCriteria(cuisine, diet, intolerance);

        if(!recipeData.results().isEmpty()) {
            return recipeData.results();
        } else {
            throw new ItemNotFoundException("No recipes found matching criteria.");
        }
    }
}
