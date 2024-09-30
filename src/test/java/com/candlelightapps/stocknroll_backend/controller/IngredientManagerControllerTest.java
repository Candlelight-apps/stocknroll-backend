package com.candlelightapps.stocknroll_backend.controller;

import com.candlelightapps.stocknroll_backend.model.Ingredient;
import com.candlelightapps.stocknroll_backend.service.IngredientManagerServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.awt.*;
import java.rmi.server.ExportException;
import java.time.LocalDate;
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

    private ObjectMapper mapper;

    List<Ingredient> ingredientList = new ArrayList<>();
    Ingredient canOfTomatoes;
    Ingredient cornflakes;
    Ingredient bread;

    @BeforeEach
    public void setup() {
        mockMvcController = MockMvcBuilders.standaloneSetup(ingredientManagerController).build();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

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
    @DisplayName("Returns empty JSON array when GET instruction sent to /ingredients endpoint when inventory is empty.")
    void testGetAllIngredients_whenIngredientRepositoryIsEmpty() throws Exception {

        ingredientList = null;

        when(mockIngredientMangerServiceImpl.getAllIngredients()).thenReturn(ingredientList);

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/stocknroll/ingredients"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(""));

    }

    @Test
    @DisplayName("Returns JSON array of Ingredients when GET instruction sent to /ingredients endpoint when inventory populated.")
    void testGetAllIngredients_whenIngredientsRepositoryContainsIngredients() throws Exception {

        ingredientList.add(canOfTomatoes);
        ingredientList.add(cornflakes);
        ingredientList.add(bread);

        when(mockIngredientMangerServiceImpl.getAllIngredients()).thenReturn(ingredientList);

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/stocknroll/ingredients"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Can of tomatoes"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Cornflakes"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].quantity").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("Wholemeal bread"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].quantity").value(1));

    }

    @Test
    @DisplayName("Returns JSON of new ingredient to be posted and returns HTTP CREATED when passed valid JSON.")
    public void testAddIngredient_WithValidJSON() throws Exception {

        when(mockIngredientMangerServiceImpl.addIngredient(canOfTomatoes)).thenReturn(canOfTomatoes);

        this.mockMvcController.perform(MockMvcRequestBuilders.post("/api/v1/stocknroll/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(canOfTomatoes)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        verify(mockIngredientMangerServiceImpl, times(1)).addIngredient(canOfTomatoes);

    }
}