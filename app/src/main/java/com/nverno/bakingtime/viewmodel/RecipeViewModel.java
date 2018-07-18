package com.nverno.bakingtime.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
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

    private MutableLiveData<Integer> selectedRecipeId = new MutableLiveData<>();
    private MutableLiveData<Integer> selectedStepId = new MutableLiveData<>();

    private LiveData<Recipe> selectedRecipe =
            Transformations.switchMap(selectedRecipeId,
                    recipeId -> recipeRepository.getRecipeById(recipeId));

    private LiveData<List<Step>> selectedRecipeSteps
            = Transformations.map(selectedRecipe, Recipe::getSteps);

    private LiveData<List<Ingredient>> selectedRecipeIngredients =
            Transformations.map(selectedRecipe, Recipe::getIngredients);

    private LiveData<Step> selectedRecipeStep
            = Transformations.switchMap(selectedStepId,
            stepId -> Transformations.map(selectedRecipeSteps, steps -> {
        for (Step step : steps) {
            if (step.getStepNumber() == stepId) {
                return step;
            }
        }
        return null;
    }));

    public RecipeViewModel(@NonNull Application app) {
        super(app);
        recipeRepository = new RecipeRepository(this.getApplication());
    }

    public void setSelectedRecipe(int recipeId) {
        selectedRecipeId.setValue(recipeId);
    }

    public void setSelectedRecipeStep(int recipeStepNumber) {
        selectedStepId.setValue(recipeStepNumber);
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipeRepository.getAll();
    }

    public LiveData<Recipe> getSelectedRecipe() {
        return selectedRecipe;
    }

    public LiveData<List<Step>> getSelectedRecipeSteps() {
        return selectedRecipeSteps;
    }

    public LiveData<List<Ingredient>> getSelectedRecipeIngredients() {
        return selectedRecipeIngredients;
    }

    public LiveData<Step> getSelectedRecipeStep() {
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
