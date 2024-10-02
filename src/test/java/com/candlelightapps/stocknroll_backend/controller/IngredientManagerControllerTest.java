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
    Ingredient invalidIngredient;
    Ingredient nullIngredient;
    Ingredient breadUpdatedQty;
    Ingredient invalidIngredientQty;

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
                .expiryDate(LocalDate.of(2025, 12, 31))
                .build();

        cornflakes = Ingredient.builder()
                .id(2L)
                .name("Cornflakes")
                .category("Breakfast cereals")
                .quantity(1)
                .expiryDate(LocalDate.of(2025, 5, 31))
                .build();

        bread = Ingredient.builder()
                .id(3L)
                .name("Wholemeal bread")
                .category("Bread")
                .quantity(1)
                .expiryDate(LocalDate.of(2024, 10, 11))
                .build();

        invalidIngredient = Ingredient.builder()
                .category("dairy")
                .quantity(1)
                .build();

        breadUpdatedQty = Ingredient.builder()
                .id(3L)
                .name("Wholemeal bread")
                .category("Bread")
                .quantity(2)
                .expiryDate(LocalDate.of(2024, 10, 11))
                .build();

        invalidIngredientQty = Ingredient.builder()
                .id(1L)
                .name("Invalid quantity")
                .category("Vegetables")
                .quantity(4)
                .expiryDate(LocalDate.of(2025, 12, 31))
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

    @Test
    @DisplayName("Returns 400 Bad Request error when invalid JSON submitted as part of POST request")
    public void testAddIngredient_insertAlbum_WithJSONMissingRequiredFields() throws Exception {

        nullIngredient = null;

        when(mockIngredientMangerServiceImpl.addIngredient(invalidIngredient)).thenReturn(nullIngredient);

        this.mockMvcController.perform(MockMvcRequestBuilders.post("/api/v1/stocknroll/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidIngredient)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    }

    @Test
    @DisplayName("Returns 404 Not Found error when passed id of ingredient that is not found.")
    public void testUpdateIngredient_WhenIdOfIngredientNotFound() throws Exception {

        nullIngredient = null;

        when(mockIngredientMangerServiceImpl.updateIngredient(1L, 2)).thenReturn(nullIngredient);

        this.mockMvcController.perform(MockMvcRequestBuilders.patch("/api/v1/stocknroll/ingredients/1")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(2)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());

    }

    @Test
    @DisplayName("Returns JSON of updated ingredient and returns HTTP OK when id of ingredient found.")
    public void testUpdateIngredient_WhenIdOfIngredientFound() throws Exception {

        when(mockIngredientMangerServiceImpl.updateIngredient(3L, 2)).thenReturn(breadUpdatedQty);

        this.mockMvcController.perform(MockMvcRequestBuilders.patch("/api/v1/stocknroll/ingredients/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(2)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(2));

        verify(mockIngredientMangerServiceImpl, times(1)).updateIngredient(3L, 2);

    }

    @Test
    @DisplayName("Returns 400 Bad Request when quantity of matched ingredient is set to less than zero.")
    public void testUpdateIngredient_WhenQuantityOfIngredientSetLessThanZero() throws Exception {

        when(mockIngredientMangerServiceImpl.updateIngredient(1L, -1)).thenReturn(invalidIngredientQty);

        this.mockMvcController.perform(MockMvcRequestBuilders.patch("/api/v1/stocknroll/ingredients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(-1)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(4));

        verify(mockIngredientMangerServiceImpl, times(1)).updateIngredient(1L, -1);

    }

    @Test
    @DisplayName("Returns JSON of deleted ingredient and returns HTTP OK when id of ingredient found.")
    public void testDeleteIngredient_WhenIdOfIngredientFound() throws Exception {

        when(mockIngredientMangerServiceImpl.deleteIngredient(1L)).thenReturn(canOfTomatoes);

        this.mockMvcController.perform(MockMvcRequestBuilders.delete("/api/v1/stocknroll/ingredients/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Can of tomatoes"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(4));

        verify(mockIngredientMangerServiceImpl, times(1)).deleteIngredient(1L);

    }

    @Test
    @DisplayName("Returns 404 Not Found and empty JSON object ingredient when id of ingredient not found.")
    public void testDeleteIngredient_WhenIdOfIngredientNotFound() throws Exception {

        when(mockIngredientMangerServiceImpl.deleteIngredient(10L)).thenReturn(nullIngredient);

        this.mockMvcController.perform(MockMvcRequestBuilders.delete("/api/v1/stocknroll/ingredients/10"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());

    }

}