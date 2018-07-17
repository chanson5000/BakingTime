package com.nverno.bakingtime.util;

import com.nverno.bakingtime.model.Recipe;

public class WidgetHelper {

    private static WidgetHelper sInstance = null;
    private Recipe mCurrentRecipe;

    private WidgetHelper() {}

    public static WidgetHelper getInstance() {
        if (sInstance == null) {
            sInstance = new WidgetHelper();
        }

        return sInstance;
    }

    public void setCurrentRecipe(Recipe recipe) {
        mCurrentRecipe = recipe;
    }

    public Recipe getCurrentRecipe() {
        return mCurrentRecipe;
    }
}
