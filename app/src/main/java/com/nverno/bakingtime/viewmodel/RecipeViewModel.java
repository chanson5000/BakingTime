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
    private MutableLiveData<Step> selectedRecipeStep;
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
        selectedRecipe = recipeRepository.getRecipeById(recipeId);
        selectedRecipeIngredients = Transformations.map(selectedRecipe, Recipe::getIngredients);
        selectedRecipeSteps = Transformations.map(selectedRecipe, Recipe::getSteps);
    }

    public void setSelectedRecipeStep(final int recipeStepId) {
        selectedRecipeStep.setValue(selectedRecipeSteps.getValue().get(recipeStepId));
    }

    public MutableLiveData<Step> getSelectedRecipeStep() {
        if (selectedRecipeStep == null) {
            selectedRecipeStep = new MutableLiveData<>();
        }
        return selectedRecipeStep;
    }

    public void nextRecipeStep() {
        if (selectedRecipeStep.getValue() != null && selectedRecipeSteps.getValue() != null) {
            if (selectedRecipeStep.getValue().getStepNumber() < selectedRecipeSteps.getValue().size()) {
                setSelectedRecipeStep(selectedRecipeStep.getValue().getStepNumber() + 1);
            }
        }
    }

    public void previousRecipeStep() {
        if (selectedRecipeStep.getValue() != null && selectedRecipeSteps.getValue() != null) {
            if (selectedRecipeStep.getValue().getStepNumber() > 0) {
                setSelectedRecipeStep(selectedRecipeStep.getValue().getStepNumber() - 1);
            }
        }
    }

    public LiveData<List<Ingredient>> getSelectedRecipeIngredients() {

        return selectedRecipeIngredients;
    }

    public LiveData<List<Step>> getSelectedRecipeSteps() {

        return selectedRecipeSteps;
    }
}
