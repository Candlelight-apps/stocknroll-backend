package com.candlelightapps.stocknroll_backend.service;

import com.candlelightapps.stocknroll_backend.model.Ingredient;
import com.candlelightapps.stocknroll_backend.repository.IngredientManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class IngredientManagerServiceImplTest {

    @Mock
    private IngredientManagerRepository mockIngredientRepository;

    @InjectMocks
    private IngredientManagerServiceImpl ingredientManagerServiceImpl;

    List<Ingredient> ingredientList = new ArrayList<>();
    Ingredient canOfTomatoes;
    Ingredient cornflakes;
    Ingredient bread;

    @BeforeEach
    public void setup() {

        canOfTomatoes = Ingredient.builder()
                .id(1L)
                .name("Can of tomatoes")
                .category("Vegetables")
                .quantity(4)
                .expiryDate(LocalDate.of(2025,12,31))
                .build();

        cornflakes = Ingredient.builder()
                .id(2L)
                .name("Cornflakes")
                .category("Breakfast cereals")
                .quantity(1)
                .expiryDate(LocalDate.of(2025,5,31))
                .build();

        bread = Ingredient.builder()
                .id(3L)
                .name("Wholemeal bread")
                .category("Bread")
                .quantity(1)
                .expiryDate(LocalDate.of(2024,10,11))
                .build();

    }

    @Test
    @DisplayName("Returns null list of Ingredients when ingredient repository is empty.")
    void testGetAllIngredients_whenIngredientRepositoryIsEmpty() {

        ingredientList = null;

        when(mockIngredientRepository.findAll()).thenReturn(ingredientList);

        List<Ingredient> actualResults = ingredientManagerServiceImpl.getAllIngredients();

        assertEquals(ingredientList, actualResults);
    }

    @Test
    @DisplayName("Returns list of all Ingredients from ingredient repository when repository is populated")
    void testGetAllIngredients_whenIngredientRepositoryIsPopulated() {

        ingredientList.add(canOfTomatoes);
        ingredientList.add(cornflakes);
        ingredientList.add(bread);

        when(mockIngredientRepository.findAll()).thenReturn(ingredientList);

        List<Ingredient> actualResults = ingredientManagerServiceImpl.getAllIngredients();

        assertEquals(ingredientList, actualResults);

    }
}