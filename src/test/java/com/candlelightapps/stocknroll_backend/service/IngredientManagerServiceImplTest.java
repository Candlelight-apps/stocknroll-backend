package com.candlelightapps.stocknroll_backend.service;

import com.candlelightapps.stocknroll_backend.exception.ParameterNotDefinedException;
import com.candlelightapps.stocknroll_backend.model.Ingredient;
import com.candlelightapps.stocknroll_backend.repository.IngredientManagerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    Ingredient invalidIngredient;
    Ingredient expiredIngredient;
    Ingredient nullIngredient;

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

        invalidIngredient = Ingredient.builder()
                .name("")
                .category("Dairy")
                .quantity(1)
                .build();

        expiredIngredient = Ingredient.builder()
                .name("Smelly cheese")
                .category("Dairy")
                .quantity(1)
                .expiryDate(LocalDate.of(1986, 4, 26))
                .build();

        nullIngredient = null;

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

    @Test
    @DisplayName("Returns ingredient that was posted when passed valid ingredient object")
    public void testAddIngredient_WhenPassedValidIngredient() {
        Ingredient expectedResult = Ingredient.builder()
                                    .id(2L)
                                    .name("corn cereal")
                                    .category("Breakfast cereals")
                                    .quantity(1)
                                    .expiryDate(LocalDate.of(2025,5,31))
                                    .imageUrl("https://img.spoonacular.com/ingredients_250x250/cornflakes.jpg")
                                    .build();
        when(mockIngredientRepository.save(expectedResult)).thenReturn(expectedResult);

        Ingredient result = ingredientManagerServiceImpl.addIngredient(cornflakes);

        verify(mockIngredientRepository, times(1)).save(expectedResult);
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Returns null ingredient when passed invalid ingredient object")
    public void testAddIngredient_WhenPassedInvalidIngredient() {

        when(mockIngredientRepository.save(invalidIngredient)).thenReturn(invalidIngredient);

        Ingredient result = ingredientManagerServiceImpl.addIngredient(invalidIngredient);

        assertNull(result);

    }

    @Test
    @DisplayName("Returns null ingredient when passed ingredient object with expiry date in the past")
    public void testAddIngredient_WhenIngredientExpiryDateExceed() {

        when(mockIngredientRepository.save(expiredIngredient)).thenReturn(expiredIngredient);

        Ingredient result = ingredientManagerServiceImpl.addIngredient(expiredIngredient);

        assertNull(result);

    }

    @Test
    @DisplayName("Returns null ingredient when passed null ingredient object")
    public void testAddIngredient_WhenIngredientIsNull() {

        when(mockIngredientRepository.save(nullIngredient)).thenReturn(nullIngredient);

        Ingredient result = ingredientManagerServiceImpl.addIngredient(nullIngredient);

        assertNull(result);

    }

    @Test
    @DisplayName("Returns ingredient with updated quantity when match found for ingredient id.")
    public void testUpdateIngredient_WhenMatchFoundForIngredientId() {

        when(mockIngredientRepository.findById(1L)).thenReturn(Optional.ofNullable(canOfTomatoes));
        ingredientManagerServiceImpl.addIngredient(canOfTomatoes);

        Ingredient result = ingredientManagerServiceImpl.updateIngredient(1L, 1);

        assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Can of tomatoes", result.getName()),
                () -> assertEquals(1, result.getQuantity()));

    }

    @Test
    @DisplayName("Returns null ingredient when no match found by ingredient id.")
    public void testUpdateIngredient_WhenMatchNoFoundForIngredientId() {

        when(mockIngredientRepository.findById(2L)).thenReturn(null);
        ingredientManagerServiceImpl.addIngredient(canOfTomatoes);

        Ingredient result = ingredientManagerServiceImpl.updateIngredient(2L, 10);

        assertNull(result);

    }

    @Test
    @DisplayName("Returns null ingredient when quantity passed is less than zero.")
    public void testUpdateIngredient_WhenQuantityPassLessThanZero() {

        when(mockIngredientRepository.findById(1L)).thenReturn(Optional.ofNullable(canOfTomatoes));
        ingredientManagerServiceImpl.addIngredient(canOfTomatoes);

        Ingredient result = ingredientManagerServiceImpl.updateIngredient(1L, -1);

        assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Invalid quantity", result.getName()),
                () -> assertEquals(4, result.getQuantity()));

    }

    @Test
    @DisplayName("Returns ingredient deleted when match found for ingredient id.")
    public void testDeleteIngredient_WhenMatchFoundForIngredientId() {

        when(mockIngredientRepository.findById(1L)).thenReturn(Optional.ofNullable(canOfTomatoes));

        Ingredient result = ingredientManagerServiceImpl.deleteIngredient(1L);

        verify(mockIngredientRepository, times(2)).findById(1L);
        verify(mockIngredientRepository, times(1)).deleteById(1L);
        assertEquals(canOfTomatoes, result);

    }

    @Test
    @DisplayName("Returns null ingredient object when no match found for ingredient id.")
    public void testDeleteIngredient_WhenMatchNoFoundForIngredientId() {

        when(mockIngredientRepository.findById(10L)).thenReturn(null);

        Ingredient result = ingredientManagerServiceImpl.deleteIngredient(10L);

        verify(mockIngredientRepository, times(1)).findById(10L);
        verify(mockIngredientRepository, times(0)).deleteById(10L);
        assertNull(result);

    }

}