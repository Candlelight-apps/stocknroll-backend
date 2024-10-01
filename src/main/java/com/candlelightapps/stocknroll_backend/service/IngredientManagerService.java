package com.candlelightapps.stocknroll_backend.service;

import com.candlelightapps.stocknroll_backend.model.Ingredient;

import java.util.List;

public interface IngredientManagerService {

    List<Ingredient> getAllIngredients();

    Ingredient addIngredient(Ingredient ingredient);

    Ingredient updateIngredient(Long id, Integer quantity);
}
