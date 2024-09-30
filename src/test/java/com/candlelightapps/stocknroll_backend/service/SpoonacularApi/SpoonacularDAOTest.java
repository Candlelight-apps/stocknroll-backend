package com.candlelightapps.stocknroll_backend.service.SpoonacularApi;

import com.candlelightapps.stocknroll_backend.model.spoonacular.recipeByCriteria.Data;
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
        assertEquals(10,data.results().size());
    }
    @Test
    @DisplayName("Return 10 recipes for valid inputs")
    void getRecipesByCriteriaTest_validInputs() {
        String cuisine = "Italian";
        String diet = "Vegan";
        String intolerances = "Egg";

        var data = SpoonacularDAO.getRecipesByCriteria(cuisine,diet,intolerances);
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
}