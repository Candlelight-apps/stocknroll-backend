package com.candlelightapps.stocknroll_backend.service.SpoonacularApi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpoonacularDAOTest {

    @Test
    @DisplayName("Return 10 recipes for all input parameters empty")
    void getRecipesByCriteriaTest_EmptyStrings() {
        String cuisine = "";
        String diet = "";
        String intolerances = "";

        var data = SpoonacularDAO.getRecipesByCriteria(cuisine,diet,intolerances);
        System.out.println("\ngetRecipesByCriteriaTest_EmptyStrings: Return 10 recipes for all input parameters empty");
        data.results().forEach(System.out::println);
        assertEquals(10,data.results().size());
    }
    @Test
    @DisplayName("Return 10 recipes for valid inputs")
    void getRecipesByCriteriaTest_validInputs() {
        String cuisine = "Italian";
        String diet = "Vegan";
        String intolerances = "Egg";

        var data = SpoonacularDAO.getRecipesByCriteria(cuisine,diet,intolerances);
        System.out.println("\ngetRecipesByCriteriaTest_validInputs:Return 10 recipes for valid inputs");
        data.results().forEach(System.out::println);
        assertEquals(10,data.results().size());
    }
    @Test
    @DisplayName("Return 10 recipes for valid lowercase inputs")
    void getRecipesByCriteriaTest_validLowercaseInputs() {
        String cuisine = "italian";
        String diet = "vegan";
        String intolerances = "egg";

        var data = SpoonacularDAO.getRecipesByCriteria(cuisine,diet,intolerances);
        assertEquals(10,data.results().size());
    }
    @Test
    @DisplayName("Return 10 recipes for valid uppercase inputs")
    void getRecipesByCriteriaTest_validUppercaseInputs() {
        String cuisine = "ITALIAN";
        String diet = "VEGAN";
        String intolerances = "EGG";

        var data = SpoonacularDAO.getRecipesByCriteria(cuisine,diet,intolerances);
        assertEquals(10,data.results().size());
    }
    @Test
    @DisplayName("Return 10 recipes for valid inputs with whitespaces")
    void getRecipesByCriteriaTest_whitespaces() {
        String cuisine = " ITALIAN";
        String diet = " VEGAN ";
        String intolerances = "   EGG   ";

        var data = SpoonacularDAO.getRecipesByCriteria(cuisine,diet,intolerances);
        assertEquals(10,data.results().size());
    }
    @Test
    @DisplayName("Return 10 recipes for null values")
    void getRecipesByCriteriaTest_null() {
        String cuisine = null;
        String diet = null;
        String intolerances = null;

        var data = SpoonacularDAO.getRecipesByCriteria(cuisine,diet,intolerances);
        assertEquals(10,data.results().size());
    }

    @Test
    @DisplayName("Return 10 recipes for null values")
    void getRecipesByIngredientsTest_null() {
        String ingredients = null;
        var data = SpoonacularDAO.getRecipesByIngredients(ingredients);

        assertEquals(10,data.results().size());
    }
    @Test
    @DisplayName("Return 10 recipes for valid input")
    void getRecipesByIngredientsTest_valid_plusSign() {
        String ingredients = "cheese,+egg";
        var data = SpoonacularDAO.getRecipesByIngredients(ingredients);
        assertEquals(10,data.results().size());
    }
    @Test
    @DisplayName("Return 10 recipes for valid input")
    void getRecipesByIngredientsTest_commaSeparated() {
        String ingredients = "cheese,egg,tomato";
        var data = SpoonacularDAO.getRecipesByIngredients(ingredients);
        System.out.println("\ngetRecipesByIngredientsTest_commaSeparated:Return 10 recipes for valid inputs");
        data.results().forEach(System.out::println);
        assertEquals(10,data.results().size());
    }

    @Test
    @DisplayName("Return null for null input")
    void testGetIngredientByName_null() {
        assertNull(SpoonacularDAO.getIngredientByName(null));
    }
    @Test
    @DisplayName("Return null for empty input")
    void testGetIngredientByName_empty() {
        assertNull(SpoonacularDAO.getIngredientByName(""));
    }
}