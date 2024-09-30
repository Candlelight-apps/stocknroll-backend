package com.candlelightapps.stocknroll_backend.controller;

import com.candlelightapps.stocknroll_backend.model.Recipe;
import com.candlelightapps.stocknroll_backend.service.RecipeManagerServiceImpl;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

    @BeforeEach
    void setUp() {
        mockMvcController = MockMvcBuilders.standaloneSetup(recipeManagerController).build();
        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Add a recipe for favourites")
    public void testAddRecipe() throws Exception {

        Recipe recipe = new Recipe(1L, 100001L, "Vegetable Lasagne", 75, "", "", true);

        when(mockRecipeManagerServiceImpl.addRecipe(recipe)).thenReturn(recipe);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/v1/stocknroll/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(recipe)))
                .andExpect(status().isCreated());

        verify(mockRecipeManagerServiceImpl, times(1)).addRecipe(recipe);
    }

}