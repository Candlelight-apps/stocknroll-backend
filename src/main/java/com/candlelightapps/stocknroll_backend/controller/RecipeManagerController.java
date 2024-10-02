package com.candlelightapps.stocknroll_backend.controller;


import com.candlelightapps.stocknroll_backend.model.Recipe;

import com.candlelightapps.stocknroll_backend.model.spoonacular.Result;
import com.candlelightapps.stocknroll_backend.service.RecipeManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RestController
@RequestMapping("/api/v1/stocknroll")
public class RecipeManagerController {

    @Autowired
    RecipeManagerService recipeManagerService;

    @PostMapping("/recipes")
    public ResponseEntity<Recipe> addRecipe(@RequestBody Recipe recipe) {
        Recipe newRecipe = recipeManagerService.addRecipe(recipe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("recipeManagerService", "/api/v1/stocknroll/" + recipe.getId());
        return new ResponseEntity<>(newRecipe, httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping("/recipes/{criteria}")
    public ResponseEntity<List<Result>> getRecipesByCriteria(@PathVariable("cuisine") String cuisine,
                                                             @PathVariable("diet") String diet,
                                                             @PathVariable("intolerances") String intolerance) {

        ArrayList<Result> recipes = recipeManagerService.getRecipeByCriteria(cuisine, diet, intolerance);

        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @GetMapping("/recipes")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeManagerService.getAllRecipes();
        return new ResponseEntity<>(recipes, HttpStatus.OK);

    }
}
