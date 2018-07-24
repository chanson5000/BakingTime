package com.nverno.bakingtime.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.nverno.bakingtime.model.Ingredient;
import com.nverno.bakingtime.model.Recipe;
import com.nverno.bakingtime.model.Step;

@Database(entities = {Recipe.class, Step.class, Ingredient.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    private static final String LOG_TAG = RecipeDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "recipes";

    private static RecipeDatabase sInstance;

    public static RecipeDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new Popular Movies database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        RecipeDatabase.class, RecipeDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting Baking Time database instance.");
        return sInstance;
    }

    public abstract RecipeDao recipeDao();

}
