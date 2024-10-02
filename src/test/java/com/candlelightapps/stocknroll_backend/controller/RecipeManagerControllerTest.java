package com.candlelightapps.stocknroll_backend.controller;

import com.candlelightapps.stocknroll_backend.exception.ItemNotFoundException;
import com.candlelightapps.stocknroll_backend.model.Recipe;

import com.candlelightapps.stocknroll_backend.model.spoonacular.Data;
import com.candlelightapps.stocknroll_backend.model.spoonacular.Result;
import com.candlelightapps.stocknroll_backend.service.RecipeManagerServiceImpl;
import com.candlelightapps.stocknroll_backend.service.SpoonacularApi.SpoonacularDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class RecipeManagerControllerTest {

    @Mock
    private RecipeManagerServiceImpl mockRecipeManagerServiceImpl;

    @InjectMocks
    private RecipeManagerController recipeManagerController;

    @Autowired
    private MockMvc mockMvcController;

    private ObjectMapper mapper;

    List<Recipe> recipeList = new ArrayList<>();
    Recipe italianVegetarian, italianVegetarianWithTomato, greekVegetarian;
    Result japaneseVeganGluten, japaneseVeganGluten2, japaneseVeganGluten3;


    @BeforeEach
    void setUp() {
        mockMvcController = MockMvcBuilders.standaloneSetup(recipeManagerController).build();
        mapper = new ObjectMapper();

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
    @DisplayName("Test Add recipe for favourites")
    public void testAddRecipe() throws Exception {

        Recipe recipe = new Recipe(1L, 664737L, "Veggie Lasagna Rolls with Peppery Pecorino Marinara", 45, "https://www.foodista.com/recipe/LYYTJFHX/veggie-lasagna-rolls-w-peppery-pecorino-marinara", "https://img.spoonacular.com/recipes/664737-312x231.jpg", true);

        when(mockRecipeManagerServiceImpl.addRecipe(recipe)).thenReturn(recipe);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/v1/stocknroll/recipes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(recipe)))
                .andExpect(status().isCreated());

        verify(mockRecipeManagerServiceImpl, times(1)).addRecipe(recipe);
    }

    @Test
    @DisplayName("Test delete of a recipe by a given Id")
    public void testDeleteRecipeById() throws Exception {
        Long recipeId = 664737L;
        String returnMsg = String.format("Recipe with id %s has been deleted successfully", recipeId);

        when(mockRecipeManagerServiceImpl.deleteRecipeById(recipeId)).thenReturn(returnMsg);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.delete("/api/v1/stocknroll/recipes/{id}", recipeId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(returnMsg));

        verify(mockRecipeManagerServiceImpl, times(1)).deleteRecipeById(recipeId);
    }

    @Test
    @DisplayName("Test delete of a recipe by an invalid Id")
    public void testDeleteRecipeByInvalidId() throws Exception {
        Long recipeId = 999999999999L;
        String returnMsg = String.format("Recipe with id %s cannot be found to be deleted", recipeId);

        when(mockRecipeManagerServiceImpl.deleteRecipeById(recipeId)).thenReturn(returnMsg);


        this.mockMvcController.perform(
                        MockMvcRequestBuilders.delete("/api/v1/stocknroll/recipes/{id}", recipeId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(returnMsg))
                .andDo(print());

        verify(mockRecipeManagerServiceImpl, times(1)).deleteRecipeById(recipeId);
    }
  
    @Test
    @DisplayName("Return 404 Not Found for invalid inputs")
    public void testGetRecipesByInValidCriteria() throws Exception {

        ArrayList<Result> recipeAL = new ArrayList<>();
        when(mockRecipeManagerServiceImpl.getRecipeByCriteria("1", "1", "1")).thenReturn(recipeAL);

       this.mockMvcController.perform(MockMvcRequestBuilders
                       .get("/api/v1/stocknroll/recipes/criteria?")
                       .param("cuisine","1")
                       .param("diet","1")
                       .param("intolerance","1")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").doesNotExist());

    }

    @Test
    @DisplayName("Test Get recipes for a given criteria")
    public void testGetRecipesByValidCriteria() throws Exception {
        ArrayList<Result> recipeAL = new ArrayList<>();
        recipeAL.add(japaneseVeganGluten);
        recipeAL.add(japaneseVeganGluten2);

        when(mockRecipeManagerServiceImpl.getRecipeByCriteria("japanese", "vegan", "gluten")).thenReturn(recipeAL);
        this.mockMvcController.perform(MockMvcRequestBuilders
                        .get("/api/v1/stocknroll/recipes/criteria?")
                        .param("cuisine","japanese")
                        .param("diet","vegan")
                        .param("intolerance","gluten")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Japanese Pickles"))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(665496L))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Yakitori Glaze"));
    }

    @Test
    @DisplayName("Return recipes for valid input")
    void testGetRecipesByIngredient_validInput() throws Exception {
        ArrayList<Result> recipeAL = new ArrayList<>();
        recipeAL.add(japaneseVeganGluten);
        recipeAL.add(japaneseVeganGluten2);

        ArrayList<String> ingredientsList = new ArrayList<>();
        ingredientsList.add("Beef");
        ingredientsList.add("egg");
        ingredientsList.add("tomatoes");
        String ingredients = "Beef,egg,tomatoes";

        when(mockRecipeManagerServiceImpl.getRecipesByIngredient(ingredientsList)).thenReturn(recipeAL);
        this.mockMvcController.perform(MockMvcRequestBuilders
                        .get("/api/v1/stocknroll/recipes/ingredient?")
                        .param("values",ingredients)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Japanese Pickles"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(665496L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Yakitori Glaze"));
    }
    @Test
    @DisplayName("Return 404 NOT_FOUND for null input")
    void testGetRecipesByIngredient_nullInput() throws Exception {
        ArrayList<Result> recipeAL = new ArrayList<>();
        recipeAL.add(japaneseVeganGluten);
        recipeAL.add(japaneseVeganGluten2);

        ArrayList<String> ingredientsList = new ArrayList<>();
        ingredientsList.add("Beef");
        ingredientsList.add("egg");
        ingredientsList.add("tomatoes");
        String ingredients = null;

        when(mockRecipeManagerServiceImpl.getRecipesByIngredient(ingredientsList)).thenReturn(recipeAL);
        this.mockMvcController.perform(MockMvcRequestBuilders
                        .get("/api/v1/stocknroll/recipes/ingredient?")
                        .param("values",ingredients)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").doesNotExist());

    }
}