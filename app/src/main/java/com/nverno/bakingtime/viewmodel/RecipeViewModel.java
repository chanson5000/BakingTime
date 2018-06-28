package com.nverno.bakingtime.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
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

import java.util.ArrayList;
import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

    private final LiveData<List<Recipe>> recipes;
//    private final LiveData<List<Ingredient>> ingredients;
//    private final LiveData<List<Step>> steps;

    private LiveData<Recipe> selectedRecipe;
    private LiveData<List<Step>> selectedRecipeSteps;
    private LiveData<List<Ingredient>> selectedRecipeIngredients;

    private RecipeRepository recipeRepository;

    public RecipeViewModel(@NonNull Application app) {
        super(app);

        recipeRepository = new RecipeRepository(this.getApplication());

        recipes = recipeRepository.getAll();
//        ingredients = recipeRepository.getAllIngredients();
//        steps = new MutableLiveData<>();

        selectedRecipe = new MediatorLiveData<>();
        selectedRecipeSteps = new MediatorLiveData<>();
        selectedRecipeIngredients = new MediatorLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
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

        // ** The alternative way...

//        selectedRecipeIngredients = Transformations.map(ingredients, new Function<List<Ingredient>, List<Ingredient>>() {
//            @Override
//            public List<Ingredient> apply(List<Ingredient> ingredients) {
//
//                List<Ingredient> selectedIngredients = new ArrayList<>();
//
//                for (Ingredient ingredient : ingredients) {
//                    if (ingredient.getRecipeId() == recipeId) {
//                        selectedIngredients.add(ingredient);
//                    }
//                }
//
//                return selectedIngredients;
//            }
//        });

        selectedRecipeSteps = recipeRepository.getStepsForRecipe(recipeId);
    }

}
