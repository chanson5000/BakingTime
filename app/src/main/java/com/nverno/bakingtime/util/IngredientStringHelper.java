package com.nverno.bakingtime.util;

import com.nverno.bakingtime.model.Ingredient;

import java.util.List;

public class IngredientStringHelper {

    public static String ListToFormattedString(List<Ingredient> ingredients) {
        StringBuilder ingredientString = new StringBuilder();

        ingredientString.append("Ingredients: ");

        for (int i = 0; i < ingredients.size(); i++) {
            if (i == ingredients.size() - 1) {
                ingredientString.append(ingredients.get(i).getName()).append(".");
            } else {
                ingredientString.append(ingredients.get(i).getName()).append(", ");
            }
        }

        String textToSet = ingredientString.toString();

        return textToSet;
    }
}
