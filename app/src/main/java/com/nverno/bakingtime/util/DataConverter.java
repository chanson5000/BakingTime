package com.nverno.bakingtime.util;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nverno.bakingtime.model.Ingredient;
import com.nverno.bakingtime.model.Step;

import java.util.List;

// Type converting for our "Embedded" object lists in Recipe model.
public class DataConverter {

    @TypeConverter
    public String fromIngredientList(List<Ingredient> ingredientList) {
        if (ingredientList == null) {
            return null;
        }

        return new Gson().toJson(ingredientList,
                new TypeToken<List<Ingredient>>(){}.getType());
    }

    @TypeConverter
    public List<Ingredient> toIngredientList(String ingredientListString) {
        if (ingredientListString == null) {
            return null;
        }

        return new Gson().fromJson(ingredientListString,
                new TypeToken<List<Ingredient>>(){}.getType());
    }

    @TypeConverter
    public String fromStepList(List<Step> stepList) {
        if (stepList == null) {
            return null;
        }

        return new Gson().toJson(stepList,
                new TypeToken<List<Step>>(){}.getType());
    }

    @TypeConverter
    public List<Step> toStepList(String stepListString) {
        if (stepListString == null) {
            return null;
        }

        return new Gson().fromJson(stepListString,
                new TypeToken<List<Step>>(){}.getType());
    }
}
