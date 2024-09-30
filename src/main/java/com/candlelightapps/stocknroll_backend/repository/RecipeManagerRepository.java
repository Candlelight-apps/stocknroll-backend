package com.candlelightapps.stocknroll_backend.repository;

import com.candlelightapps.stocknroll_backend.model.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeManagerRepository extends CrudRepository<Recipe, Long> {

}
