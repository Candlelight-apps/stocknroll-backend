package com.candlelightapps.stocknroll_backend.repository;

import com.candlelightapps.stocknroll_backend.model.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientManagerRepository extends CrudRepository<Ingredient, Long> {
}
