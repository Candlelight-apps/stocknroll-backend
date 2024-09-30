package com.candlelightapps.stocknroll_backend.controller;

import com.candlelightapps.stocknroll_backend.model.Ingredient;
import com.candlelightapps.stocknroll_backend.service.IngredientManagerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
class IngredientManagerControllerTest {

    @Mock
    private IngredientManagerServiceImpl mockIngredientMangerServiceImpl;

    @InjectMocks
    private IngredientManagerController ingredientManagerController;

    @Autowired
    private MockMvc mockMvcController;

    List<Ingredient> ingredientList = new ArrayList<>();

    @BeforeEach
    public void setup() {
        mockMvcController = MockMvcBuilders.standaloneSetup(ingredientManagerController).build();




    }

    @Test
    @DisplayName("Returns empty JSON array when GET instruction send to /ingredients endpoint when inventory is empty.")
    void testGetAllIngredients() throws Exception {

        ingredientList = null;

        when(mockIngredientMangerServiceImpl.getAllIngredients()).thenReturn(ingredientList);

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/ingredients"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(""));

    }
}