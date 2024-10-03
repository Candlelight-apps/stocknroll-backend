package com.candlelightapps.stocknroll_backend.service.SpoonacularApi;

import java.util.ArrayList;

public class IngredientHandler {

    public static String convertListToString(ArrayList<String> ingredientList) {
        String ingredients = "";
        StringBuilder stringBuilder = new StringBuilder(ingredients);
        if (ingredientList == null) {
            return ingredients;
        }
        if (!ingredientList.isEmpty()) {
            stringBuilder.append(ingredientList.getFirst());
            for (int i = 1; i < ingredientList.size() ; i++) {
                stringBuilder.append(",");
                stringBuilder.append(ingredientList.get(i));
            }

            ingredients = stringBuilder.toString();
        }

        return ingredients;
    }
}
