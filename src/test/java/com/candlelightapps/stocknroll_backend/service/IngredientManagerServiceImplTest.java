package com.candlelightapps.stocknroll_backend.service;

import com.candlelightapps.stocknroll_backend.model.Ingredient;
import com.candlelightapps.stocknroll_backend.repository.IngredientManagerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class IngredientManagerServiceImplTest {

    @Mock
    private IngredientManagerRepository mockIngredientRepository;

    @InjectMocks
    private IngredientManagerServiceImpl ingredientManagerServiceImpl;

    List<Ingredient> ingredientList;

    @Test
    @DisplayName("Returns null list of Ingredients when ingredient repository is empty.")
    void testGetAllIngredients_whenIngredientRepositoryIsEmpty() {

        ingredientList = null;

        when(mockIngredientRepository.findAll()).thenReturn(ingredientList);

        List<Ingredient> actualResults = ingredientManagerServiceImpl.getAllIngredients();

        assertEquals(ingredientList, actualResults);
    }
}