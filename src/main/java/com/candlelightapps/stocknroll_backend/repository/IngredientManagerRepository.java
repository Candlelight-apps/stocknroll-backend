package com.candlelightapps.stocknroll_backend.repository;

import com.candlelightapps.stocknroll_backend.model.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientManagerRepository extends CrudRepository<Ingredient, Long> {

}
