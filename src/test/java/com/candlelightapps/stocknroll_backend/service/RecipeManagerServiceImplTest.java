package com.candlelightapps.stocknroll_backend.service;

import com.candlelightapps.stocknroll_backend.exception.ItemNotFoundException;
import com.candlelightapps.stocknroll_backend.model.Ingredient;
import com.candlelightapps.stocknroll_backend.model.Recipe;

import com.candlelightapps.stocknroll_backend.model.spoonacular.Data;
import com.candlelightapps.stocknroll_backend.model.spoonacular.Result;
import com.candlelightapps.stocknroll_backend.repository.RecipeManagerRepository;
import com.candlelightapps.stocknroll_backend.service.SpoonacularApi.SpoonacularDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class RecipeManagerServiceImplTest {

    @Mock
    private RecipeManagerRepository mockRecipeRepository;

    @InjectMocks
    private RecipeManagerServiceImpl recipeManagerServiceImpl;

    Recipe italianVegetarian, italianVegetarianWithTomato, greekVegetarian;
    Result japaneseVeganGluten, japaneseVeganGluten2, japaneseVeganGluten3;

    @BeforeEach
    void setUp() {
        italianVegetarian = Recipe.builder()
                .id(1L)
                .recipeId(715769L)
                .title("Broccolini Quinoa Pilaf")
                .image("https://img.spoonacular.com/recipes/715769-312x231.jpg")
                .favourite(true)
                .readyInMinutes(30)
                .sourceUrl("https://pickfreshfoods.com/broccolini-quinoa-pilaf/")
                .build();

        italianVegetarianWithTomato = Recipe.builder()
                .id(2L)
                .recipeId(648275L)
                .title("Italian Tomato and Mozzarella Caprese")
                .image("https://img.spoonacular.com/recipes/648275-312x231.jpg")
                .favourite(true)
                .readyInMinutes(45)
                .sourceUrl("https://www.foodista.com/recipe/MK4TM4WZ/italian-tomato-and-mozzarella-caprese")
                .build();

        greekVegetarian = Recipe.builder()
                .id(3L)
                .recipeId(649886L)
                .title("Lemony Greek Lentil Soup")
                .image("https://img.spoonacular.com/recipes/649886-312x231.jpg")
                .favourite(true)
                .readyInMinutes(45)
                .sourceUrl("https://www.foodista.com/recipe/KV6GLYDK/lemony-greek-lentil-soup")
                .build();

        japaneseVeganGluten = new Result(648487,"Japanese Pickles", "https://img.spoonacular.com/recipes/648487-312x231.jpg", "jpg", 45, "http://www.foodista.com/recipe/HBL5F77J/japanese-pickles");
        japaneseVeganGluten2 = new Result(665496, "Yakitori Glaze", "https://img.spoonacular.com/recipes/665496-312x231.jpg", "jpg", 45, "https://spoonacular.com/yakitori-glaze-665496");
        japaneseVeganGluten3 = new Result(37513,"Japanese Salad Dressing", "https://img.spoonacular.com/recipes/37513-312x231.jpg", "jpg", 12, "https://spoonacular.com/japanese-salad-dressing-37513");
    }

    @Test
    @DisplayName("Return recipes based on the criteria")
    public void testGetRecipeByCriteria_whenMatchFound() {

        List<Result> recipeList = new ArrayList<>();
        recipeList.add(japaneseVeganGluten);
        recipeList.add(japaneseVeganGluten2);
        recipeList.add(japaneseVeganGluten3);

        List<Result> actualResults = recipeManagerServiceImpl
                .getRecipeByCriteria("japanese", "vegan", "gluten");

        assertEquals(recipeList.size(), actualResults.size());

    }

    @Test
    @DisplayName("Return an Item Not Found error msg on invalid criteria")
    public void testGetRecipeByCriteria_withInvalidCriteriaThrowsError() {
        String errMsg = "No recipes found matching criteria.";

        try {
            recipeManagerServiceImpl.getRecipeByCriteria("2", "1", "3");
        } catch (ItemNotFoundException infe) {
            assertThat(infe).hasMessageContaining(errMsg);
        }
    }

    @Test
    @DisplayName("Test Delete Recipe throws an exception with an Invalid Id ")
    public void testDeleteRecipeByIdThrowsItemNotFoundException() {
        Long recipeId = 715769L;
        String expectedMsg = String.format("Recipe with id %s cannot be found to be deleted", recipeId);
        String actualMsg;

        when(mockRecipeRepository.findById(recipeId)).thenReturn(null);
        try {
            actualMsg = recipeManagerServiceImpl.deleteRecipeById(recipeId);
        } catch (ItemNotFoundException infe) {
            actualMsg = infe.getMessage();
        }
        verify(mockRecipeRepository, times(1)).findById(recipeId);
        verify(mockRecipeRepository, times(0)).deleteById(recipeId);

        assertTrue(actualMsg.contains(expectedMsg));
    }

    @Test
    @DisplayName("Test Delete Recipe with Valid Id ")
    public void testSuccessfulDeleteRecipeById() {
        Long recipeId = 715769L;
        when(mockRecipeRepository.findById(715769L)).thenReturn(Optional.ofNullable(italianVegetarian));

        String actualMsg = recipeManagerServiceImpl.deleteRecipeById(recipeId);
        String expectedMessage = String.format("Recipe with id %s has been deleted successfully", recipeId);

        assertTrue(actualMsg.contains(expectedMessage));
    }

    @Test
    @DisplayName("Return recipes for 1 valid ingredient")
    void testGetRecipesByIngredient_1validIngredient() {
        ArrayList<Result> recipeList = new ArrayList<>();
        recipeList.add(japaneseVeganGluten);
        recipeList.add(japaneseVeganGluten2);
        recipeList.add(japaneseVeganGluten3);
        Data data = new Data(recipeList);

        ArrayList<String> ingredients = new ArrayList<>(List.of("Beef"));

        try (MockedStatic<SpoonacularDAO> mockSpoonacularDAO = Mockito.mockStatic(SpoonacularDAO.class)) {
            mockSpoonacularDAO.when(() -> SpoonacularDAO.getRecipesByIngredients("Beef"))
                    .thenReturn(data);
            assertEquals(3,SpoonacularDAO.getRecipesByIngredients("Beef").results().size());
            List<Result> actualResults = recipeManagerServiceImpl
                    .getRecipesByIngredient(ingredients);
            assertEquals(recipeList.size(), actualResults.size());

        }




    }}

