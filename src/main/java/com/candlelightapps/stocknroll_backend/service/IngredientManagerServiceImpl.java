package com.candlelightapps.stocknroll_backend.service;

import com.candlelightapps.stocknroll_backend.repository.IngredientManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientManagerServiceImpl implements IngredientManagerService {

    @Autowired
    IngredientManagerRepository ingredientManagerRepository;
}
