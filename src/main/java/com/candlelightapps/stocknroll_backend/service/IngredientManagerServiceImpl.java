package com.candlelightapps.stocknroll_backend.service;

import com.candlelightapps.stocknroll_backend.exception.ItemNotFoundException;
import com.candlelightapps.stocknroll_backend.exception.ParameterNotDefinedException;
import com.candlelightapps.stocknroll_backend.model.Ingredient;
import com.candlelightapps.stocknroll_backend.model.spoonacular.ingredient.DataIngredient;
import com.candlelightapps.stocknroll_backend.repository.IngredientManagerRepository;
import com.candlelightapps.stocknroll_backend.service.SpoonacularApi.SpoonacularDAO;
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

        try {
            try {
                ingredientRepository.findAll().forEach(ingredientList::add);
                return ingredientList;

            } catch (NullPointerException e) {
                throw new ItemNotFoundException("Repository empty.");

            }
        } catch (ItemNotFoundException e) {
            ingredientList = null;

        }
        return ingredientList;
    }

    @Override
    public Ingredient addIngredient(Ingredient ingredient) {
        Ingredient ingredientAdded = null;

        try {
            if (ingredient.getName() == null || ingredient.getCategory() == null || ingredient.getExpiryDate() == null) {
                throw new NullPointerException("One or more ingredient fields are null");
            }

        } catch (NullPointerException e) {
            return ingredientAdded;
        }

        try {
            if (ingredient.getName().isEmpty() || ingredient.getCategory().isEmpty() || ingredient.getQuantity() == 0) {
                throw new ParameterNotDefinedException("Missing/invalid field(s) in ingredient.");

            } else if (ingredient.getExpiryDate().isBefore(LocalDate.now())) {
                throw new ParameterNotDefinedException("Expiry date of ingredient cannot be in past.");
            }

            DataIngredient data = SpoonacularDAO.getIngredientByName(ingredient.getName());
            if(data!=null){
                if(data.results()!=null && !data.results().isEmpty()) {
                    ingredient.setName(data.results().getFirst().name());
                    String imageUrl = convertStringToImageUrl(data.results().getFirst().image());
                    ingredient.setImageUrl(imageUrl);
                    ingredientRepository.save(ingredient);
                    ingredientAdded = ingredient;
                }

            }else{
                throw new ParameterNotDefinedException("Invalid ingredient.");
            }

        } catch (ParameterNotDefinedException e) {
            return ingredientAdded;
        }

        return ingredientAdded;
    }

    private String convertStringToImageUrl(String image) {
      return  "https://img.spoonacular.com/ingredients_100x100/"+image;
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

        try {
            try {
                if (ingredientRepository.findById(id).isPresent()) {
                    deletedIngredient = ingredientRepository.findById(id).get();
                    ingredientRepository.deleteById(id);

                }
            } catch (NullPointerException e) {
                throw new ItemNotFoundException("No ingredient found with that id");

            }
        } catch (ItemNotFoundException e) {
            deletedIngredient = null;

        }
     return deletedIngredient;

    }
}

