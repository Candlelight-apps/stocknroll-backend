package com.candlelightapps.stocknroll_backend.controller;


import com.candlelightapps.stocknroll_backend.model.Ingredient;
import com.candlelightapps.stocknroll_backend.service.IngredientManagerService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stocknroll/ingredients")
public class IngredientManagerController {

    @Autowired
    IngredientManagerService ingredientManagerService;

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        List<Ingredient> ingredientList = ingredientManagerService.getAllIngredients();
        return new ResponseEntity<>(ingredientList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Ingredient> addIngredient(@RequestBody Ingredient ingredient) {
        Ingredient ingredientAdded = ingredientManagerService.addIngredient(ingredient);

        if (ingredientAdded == null) {
            return new ResponseEntity<>(ingredientAdded, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ingredientAdded, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ingredient> updateIngredient(@PathVariable Long id, @RequestBody Integer quantity) {
        Ingredient updatedIngredient = ingredientManagerService.updateIngredient(id, quantity);

        if (updatedIngredient == null) {
            return new ResponseEntity<>(updatedIngredient, HttpStatus.NOT_FOUND);

        } else if (updatedIngredient.getName().equals("Invalid quantity")) {
            return new ResponseEntity<>(updatedIngredient, HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<>(updatedIngredient, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Ingredient> deleteIngredient(@PathVariable Long id) {
        Ingredient deletedIngredient = ingredientManagerService.deleteIngredient(id);
        return new ResponseEntity<>(deletedIngredient, HttpStatus.OK);
    }

}
