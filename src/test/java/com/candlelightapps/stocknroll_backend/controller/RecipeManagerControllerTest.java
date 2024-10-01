package com.candlelightapps.stocknroll_backend.controller;

import com.candlelightapps.stocknroll_backend.exception.ItemNotFoundException;
import com.candlelightapps.stocknroll_backend.model.Recipe;

import com.candlelightapps.stocknroll_backend.model.spoonacular.Result;
import com.candlelightapps.stocknroll_backend.service.RecipeManagerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
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

        populateRecipes();

        populateResults();
    }

    @Test
    @DisplayName("Test Add Recipe for favourites")
    public void testAddRecipe() throws Exception {

        Recipe recipe = new Recipe(4L, 664737L, "Veggie Lasagna Rolls with Peppery Pecorino Marinara", 45, "https://www.foodista.com/recipe/LYYTJFHX/veggie-lasagna-rolls-w-peppery-pecorino-marinara", "https://img.spoonacular.com/recipes/664737-312x231.jpg", true);

        when(mockRecipeManagerServiceImpl.addRecipe(recipe)).thenReturn(recipe);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/v1/stocknroll/recipes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(recipe)))
                .andExpect(status().isCreated());

        verify(mockRecipeManagerServiceImpl, times(1)).addRecipe(recipe);
    }

    @Test
    @DisplayName("Test Get All Recipes saved as favourites")
    public void testGetAllRecipes_whenPopulated() throws Exception {
        List<Recipe> recipes = populateRecipes();

        when(mockRecipeManagerServiceImpl.getAllRecipes()).thenReturn(recipes);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.get("/api/v1/stocknroll/recipes"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].recipeId").value(715769))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Broccolini Quinoa Pilaf"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].readyInMinutes").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sourceUrl").value("https://pickfreshfoods.com/broccolini-quinoa-pilaf/"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].recipeId").value(648275))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Italian Tomato and Mozzarella Caprese"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].readyInMinutes").value(45))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sourceUrl").value("https://www.foodista.com/recipe/MK4TM4WZ/italian-tomato-and-mozzarella-caprese"));
    }

    @Test
    @DisplayName("Test Get All Recipes without any saved to throw a helpful message")
    public void testGetAllRecipes_whenUnpopulated() throws Exception {

        when(mockRecipeManagerServiceImpl.getAllRecipes()).thenReturn(null);

        try {
            this.mockMvcController.perform(
                            MockMvcRequestBuilders.get("/api/v1/stocknroll/recipes"))
                    .andExpect(status().isOk());
        } catch (ItemNotFoundException infe) {
            Assertions.assertEquals("There are no saved recipes!", infe.getMessage());
        }
    }

    private List<Recipe> populateRecipes() {
        italianVegetarian = Recipe.builder()
                .id(1L).recipeId(715769L).title("Broccolini Quinoa Pilaf")
                .image("https://img.spoonacular.com/recipes/715769-312x231.jpg")
                .isFavourite(true).readyInMinutes(30)
                .sourceUrl("https://pickfreshfoods.com/broccolini-quinoa-pilaf/")
                .build();

        italianVegetarianWithTomato = Recipe.builder()
                .id(2L).recipeId(648275L).title("Italian Tomato and Mozzarella Caprese")
                .image("https://img.spoonacular.com/recipes/648275-312x231.jpg")
                .isFavourite(true).readyInMinutes(45)
                .sourceUrl("https://www.foodista.com/recipe/MK4TM4WZ/italian-tomato-and-mozzarella-caprese")
                .build();

        greekVegetarian = Recipe.builder()
                .id(3L).recipeId(649886L).title("Lemony Greek Lentil Soup")
                .image("https://img.spoonacular.com/recipes/649886-312x231.jpg")
                .isFavourite(true).readyInMinutes(45)
                .sourceUrl("https://www.foodista.com/recipe/KV6GLYDK/lemony-greek-lentil-soup")
                .build();

        recipeList.add(italianVegetarian);
        recipeList.add(italianVegetarianWithTomato);
        recipeList.add(greekVegetarian);

        return recipeList;
    }

    private void populateResults() {
        japaneseVeganGluten = new Result(648487,"Japanese Pickles", "https://img.spoonacular.com/recipes/648487-312x231.jpg", "jpg", 45, "http://www.foodista.com/recipe/HBL5F77J/japanese-pickles");
        japaneseVeganGluten2 = new Result(665496, "Yakitori Glaze", "https://img.spoonacular.com/recipes/665496-312x231.jpg", "jpg", 45, "https://spoonacular.com/yakitori-glaze-665496");
        japaneseVeganGluten3 = new Result(37513,"Japanese Salad Dressing", "https://img.spoonacular.com/recipes/37513-312x231.jpg", "jpg", 12, "https://spoonacular.com/japanese-salad-dressing-37513");
    }


/*
    @Test
    @DisplayName("Test Get recipes for a given criteria")
    public void testGetRecipesByValidCriteria() throws Exception {

        ArrayList<Result> recipeAL = new ArrayList<>();
        recipeAL.add(japaneseVeganGluten);
        recipeAL.add(japaneseVeganGluten2);

        when(mockRecipeManagerServiceImpl.getRecipeByCriteria("Japanese", "vegetarian", "gluten")).thenReturn(recipeAL);

       this.mockMvcController.perform(MockMvcRequestBuilders
               .get("/api/v1/stocknroll/recipes/cuisine=japanese&diet=vegetarian&intolerances=gluten"))
//               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(648487L))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Japanese Pickles"))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(665496L))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Yakitori Glaze"));
    }

 */

}