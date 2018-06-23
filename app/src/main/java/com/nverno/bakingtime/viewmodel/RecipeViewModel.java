package com.nverno.bakingtime.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.nverno.bakingtime.model.Recipe;
import com.nverno.bakingtime.repository.RecipeRepository;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

    private final LiveData<List<Recipe>> recipes;

    private LiveData<Recipe> selectedRecipe;

    public RecipeViewModel(@NonNull Application app) {
        super(app);

        RecipeRepository recipeRepository = new RecipeRepository(this.getApplication());

        recipes = recipeRepository.getAll();

        selectedRecipe = new MediatorLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public LiveData<Recipe> getSelectedRecipe() {
        return selectedRecipe;
    }

    public void setSelectedRecipe(final int recipeId) {
        selectedRecipe = Transformations.map(recipes, new Function<List<Recipe>, Recipe>() {
            @Override
            public Recipe apply(List<Recipe> recipes) {
                for (Recipe recipe : recipes) {
                    if (recipe.getId() == recipeId) {
                        return recipe;
                    }
                }
                return null;
            }
        });
    }

}
