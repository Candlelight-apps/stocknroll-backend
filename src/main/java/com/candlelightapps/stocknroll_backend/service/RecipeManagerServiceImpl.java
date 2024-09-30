package com.candlelightapps.stocknroll_backend.service;

import com.candlelightapps.stocknroll_backend.repository.RecipeManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeManagerServiceImpl implements RecipeManagerService {

    @Autowired
    RecipeManagerRepository recipeManagerRepository;
}
