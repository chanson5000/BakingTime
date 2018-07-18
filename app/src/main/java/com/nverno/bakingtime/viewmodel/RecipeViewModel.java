package com.nverno.bakingtime.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.nverno.bakingtime.model.Ingredient;
import com.nverno.bakingtime.model.Recipe;
import com.nverno.bakingtime.model.Step;
import com.nverno.bakingtime.repository.RecipeRepository;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

    private RecipeRepository recipeRepository;

    private LiveData<List<Recipe>> recipes;
    private LiveData<Recipe> selectedRecipe;
    private LiveData<Step> selectedRecipeStep;

    private LiveData<List<Step>> selectedRecipeSteps;
    private LiveData<List<Ingredient>> selectedRecipeIngredients;

    public RecipeViewModel(@NonNull Application app) {
        super(app);

        recipeRepository = new RecipeRepository(this.getApplication());

        recipes = recipeRepository.getAll();
    }

    public void setSelectedRecipe(final int recipeId) {
        selectedRecipe = recipeRepository.getRecipeById(recipeId);
        setSelectedRecipeSteps();
        setSelectedRecipeIngredients();
    }

    private void setSelectedRecipeSteps() {
        selectedRecipeSteps = Transformations.map(selectedRecipe, Recipe::getSteps);
    }

    private void setSelectedRecipeIngredients() {
        selectedRecipeIngredients = Transformations.map(selectedRecipe, Recipe::getIngredients);
    }

    public void setSelectedRecipeStep(final int recipeStepNumber) {
        selectedRecipeStep = Transformations.map(selectedRecipeSteps, steps -> {
            for (Step step : steps) {
                if (step.getStepNumber() == recipeStepNumber) {
                    return step;
                }
            }
            return null;
        });
    }

    public LiveData<List<Recipe>> getRecipes() {
        if (recipes == null) {
            recipes = new MediatorLiveData<>();
        }
        return recipes;
    }

    public LiveData<Recipe> getSelectedRecipe() {
        if (selectedRecipe == null) {
            selectedRecipe = new MediatorLiveData<>();
        }
        return selectedRecipe;
    }

    public LiveData<List<Step>> getSelectedRecipeSteps() {
        if (selectedRecipeSteps == null) {
            selectedRecipe = new MediatorLiveData<>();
        }
        return selectedRecipeSteps;
    }

    public LiveData<List<Ingredient>> getSelectedRecipeIngredients() {
        if (selectedRecipeIngredients == null) {
            selectedRecipeIngredients = new MediatorLiveData<>();
        }
        return selectedRecipeIngredients;
    }

    public LiveData<Step> getSelectedRecipeStep() {
        if (selectedRecipeStep == null) {
            selectedRecipeStep = new MediatorLiveData<>();
        }
        return selectedRecipeStep;
    }

    public void nextRecipeStep() {
        if (selectedRecipeStep.getValue() != null) {
            setSelectedRecipeStep(selectedRecipeStep.getValue().getStepNumber() + 1);
        }
    }

    public void previousRecipeStep() {
        if (selectedRecipeStep.getValue() != null) {
            setSelectedRecipeStep(selectedRecipeStep.getValue().getStepNumber() - 1);
        }
    }
}
