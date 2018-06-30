package com.nverno.bakingtime.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nverno.bakingtime.model.Ingredient;
import com.nverno.bakingtime.model.Recipe;
import com.nverno.bakingtime.model.Step;
import com.nverno.bakingtime.repository.RecipeRepository;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

    private RecipeRepository recipeRepository;

    private LiveData<List<Recipe>> recipes;
    private LiveData<Recipe> selectedRecipe;
    private MediatorLiveData<Step> selectedRecipeStep;
    private LiveData<List<Step>> selectedRecipeSteps;
    private LiveData<List<Ingredient>> selectedRecipeIngredients;

    public RecipeViewModel(@NonNull Application app) {
        super(app);

        recipeRepository = new RecipeRepository(this.getApplication());

        recipes = recipeRepository.getAll();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public LiveData<Recipe> getSelectedRecipe() {
        if (selectedRecipe == null) {
            selectedRecipe = new MediatorLiveData<>();
        }
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

        selectedRecipeIngredients = recipeRepository.getIngredientsForRecipe(recipeId);
        selectedRecipeSteps = recipeRepository.getStepsForRecipe(recipeId);
    }

    public LiveData<Step> getSelectedRecipeStep() {
        if (selectedRecipeStep == null) {
            selectedRecipeStep = new MediatorLiveData<>();
        }
        return selectedRecipeStep;
    }

    public void setSelectedRecipeStep(final int recipeStepId) {
        if (selectedRecipeStep == null) {
            selectedRecipeStep = new MediatorLiveData<>();
        }
        selectedRecipeStep.removeSource(selectedRecipeSteps);
        selectedRecipeStep.addSource(selectedRecipeSteps, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                if (steps != null && !steps.isEmpty()) {
                    for (Step step : steps) {
                        if (step.getStep() == recipeStepId) {
                            selectedRecipeStep.setValue(step);
                        }
                    }
                }
            }
        });
    }

    public LiveData<List<Ingredient>> getSelectedRecipeIngredients() {
        return selectedRecipeIngredients;
    }

    public LiveData<List<Step>> getSelectedRecipeSteps() {
        return selectedRecipeSteps;
    }
}
