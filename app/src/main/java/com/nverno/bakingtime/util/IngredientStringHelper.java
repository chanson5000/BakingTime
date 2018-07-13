package com.nverno.bakingtime.util;

import android.content.Context;

import com.nverno.bakingtime.RecipeIngredientsWidget;
import com.nverno.bakingtime.WidgetUpdateService;
import com.nverno.bakingtime.model.Ingredient;

import java.util.List;

public class IngredientStringHelper {

    private static IngredientStringHelper INSTANCE = null;
    private String mCurrentIngredients;
    private String mCurrentRecipeName;

    private IngredientStringHelper() {}

    public static IngredientStringHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IngredientStringHelper();
        }
        return INSTANCE;
    }

    public String getCurrentIngredientsString() {
        if (mCurrentIngredients != null) {
            return mCurrentIngredients;
        } else {
            return null;
        }
    }

    public String getCurrentRecipeName() {
        if (mCurrentRecipeName != null) {
            return mCurrentRecipeName;
        } else {
            return null;
        }
    }

    public void setCurrentIngredientsString(Context context, String currentIngredients) {
        mCurrentIngredients = currentIngredients;

        WidgetUpdateService.startActionUpdateRecipeWidgets(context);
    }

    public String ListToFormattedString(Context context, List<Ingredient> ingredients) {
        StringBuilder ingredientString = new StringBuilder();

        ingredientString.append("Ingredients: ");

        for (int i = 0; i < ingredients.size(); i++) {
            if (i == ingredients.size() - 1) {
                ingredientString.append(ingredients.get(i).getName()).append(".");
            } else {
                ingredientString.append(ingredients.get(i).getName()).append(", ");
            }
        }

        String currentIngredients = ingredientString.toString();

        getInstance().setCurrentIngredientsString(context, currentIngredients);

        return currentIngredients;
    }

    public void setCurrentRecipename(Context context, String currentRecipeName) {
        mCurrentRecipeName = currentRecipeName;

        WidgetUpdateService.startActionUpdateRecipeWidgets(context);
    }
}
