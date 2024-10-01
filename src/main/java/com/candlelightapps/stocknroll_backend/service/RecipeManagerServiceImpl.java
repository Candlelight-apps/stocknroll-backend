package com.candlelightapps.stocknroll_backend.service;

import com.candlelightapps.stocknroll_backend.exception.ItemNotFoundException;
import com.candlelightapps.stocknroll_backend.model.Recipe;

import com.candlelightapps.stocknroll_backend.model.spoonacular.Data;
import com.candlelightapps.stocknroll_backend.model.spoonacular.Result;
import com.candlelightapps.stocknroll_backend.repository.IngredientManagerRepository;
import com.candlelightapps.stocknroll_backend.repository.RecipeManagerRepository;
import com.candlelightapps.stocknroll_backend.service.SpoonacularApi.SpoonacularDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

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

    @Override
    public String deleteRecipeById(Long recipeId) {
        Optional<Recipe> recipe = recipeManagerRepository.findById(recipeId);

        if(recipe.isPresent()) {
            recipeManagerRepository.deleteById(recipeId);
            return String.format("Recipe with id %s has been deleted successfully", recipeId);
        } else {
            throw new ItemNotFoundException(String.format("Recipe with id %s cannot be found to be deleted", recipeId));
        }
    }
}
