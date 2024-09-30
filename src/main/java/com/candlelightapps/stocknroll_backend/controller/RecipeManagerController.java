package com.candlelightapps.stocknroll_backend.controller;


import com.candlelightapps.stocknroll_backend.model.Recipe;
import com.candlelightapps.stocknroll_backend.service.RecipeManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@RestController
@RequestMapping("/api/v1/stocknroll")
public class RecipeManagerController {

    @Autowired
    RecipeManagerService recipeManagerService;

    @PostMapping
    public ResponseEntity<Recipe> addAlbum(@RequestBody Recipe recipe) {
        Recipe newRecipe = recipeManagerService.addRecipe(recipe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("recipeManagerService", "/api/v1/stocknroll/" + recipe.getId());
        return new ResponseEntity<>(newRecipe, httpHeaders, HttpStatus.OK);
    }

}
