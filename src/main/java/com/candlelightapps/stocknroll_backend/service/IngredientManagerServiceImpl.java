package com.candlelightapps.stocknroll_backend.service;

import com.candlelightapps.stocknroll_backend.exception.ItemNotFoundException;
import com.candlelightapps.stocknroll_backend.exception.ParameterNotDefinedException;
import com.candlelightapps.stocknroll_backend.model.Ingredient;
import com.candlelightapps.stocknroll_backend.repository.IngredientManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class IngredientManagerServiceImpl implements IngredientManagerService {

    @Autowired
    IngredientManagerRepository ingredientRepository;

    @Override
    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientRepository.findAll().forEach(ingredientList::add);
        return ingredientList;
    }

    @Override
    public Ingredient addIngredient(Ingredient ingredient) {
        Ingredient ingredientAdded;

        try {
            if (ingredient.getName() == null || ingredient.getCategory() == null || ingredient.getExpiryDate() == null) {
                throw new NullPointerException("One or more ingredient fields are null");
            }

        } catch (NullPointerException e) {
            ingredientAdded = null;
            return ingredientAdded;
        }

        try {
            if (ingredient.getName().isEmpty() || ingredient.getCategory().isEmpty() || ingredient.getQuantity() == 0) {
                throw new ParameterNotDefinedException("Missing/invalid field(s) in ingredient.");

            } else if (ingredient.getExpiryDate().isBefore(LocalDate.now())) {
                throw new ParameterNotDefinedException("Expiry date of ingredient cannot be in past.");
            }

            ingredientRepository.save(ingredient);
            ingredientAdded = ingredient;

        } catch (ParameterNotDefinedException e) {
            ingredientAdded = null;
        }

        return ingredientAdded;
    }

    @Override
    public Ingredient updateIngredient(Long id, Integer quantity) {

        Ingredient updatedIngredient = new Ingredient();

        try {
            try {
                if (ingredientRepository.findById(id).isPresent()) {
                    updatedIngredient = ingredientRepository.findById(id).get();

                    if (quantity < 0) {
                        updatedIngredient.setName("Invalid quantity");
                        return updatedIngredient;

                    }
                    updatedIngredient.setQuantity(quantity);
                    ingredientRepository.save(updatedIngredient);
                    return updatedIngredient;

                }
            } catch (NullPointerException e) {
                throw new ItemNotFoundException("No ingredient found with that id");

            }
        } catch (ItemNotFoundException e) {
            updatedIngredient = null;

        }
        return updatedIngredient;
    }

    @Override
    public Ingredient deleteIngredient(Long id) {

        Ingredient deletedIngredient = new Ingredient();

        if (ingredientRepository.findById(id).isPresent()) {
            deletedIngredient = ingredientRepository.findById(id).get();
            ingredientRepository.deleteById(id);

        }
     return deletedIngredient;
    }
}

