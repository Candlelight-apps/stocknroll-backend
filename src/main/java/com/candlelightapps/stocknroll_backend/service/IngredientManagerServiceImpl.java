package com.candlelightapps.stocknroll_backend.service;

import com.candlelightapps.stocknroll_backend.model.Ingredient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientManagerServiceImpl implements IngredientManagerService {
    @Override
    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredientList = null;
        return ingredientList;
    }
}
