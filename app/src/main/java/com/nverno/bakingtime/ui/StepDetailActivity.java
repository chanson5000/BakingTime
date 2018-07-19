package com.nverno.bakingtime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.util.FragmentViewChanger;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

public class StepDetailActivity extends AppCompatActivity {

    private static final String RECIPE_ID = "RECIPE_ID";
    private static final String STEP_ID = "STEP_ID";

    private RecipeViewModel mRecipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            if (findViewById(R.id.view_media_only) != null && actionBar.isShowing()) {
                actionBar.hide();
            } else if (!actionBar.isShowing()) {
                actionBar.show();
            }
        }

        Intent parentIntent = getIntent();

        if (parentIntent != null
                && parentIntent.hasExtra(RECIPE_ID)
                && parentIntent.hasExtra(STEP_ID)) {

            Bundle bundle = parentIntent.getExtras();

            if (bundle != null) {
                initViewModel(bundle.getInt(RECIPE_ID), bundle.getInt(STEP_ID));
            }
            // Remove extras to prevent resetting our ViewModel.
            parentIntent.removeExtra(RECIPE_ID);
            parentIntent.removeExtra(STEP_ID);
        } else {

            initViewModel();
        }
    }

    private void initViewModel() {
        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        setTitleText();
    }

    // Initialize the view model to set the recipe and the current step.
    private void initViewModel(int recipeId, int stepId) {
        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        mRecipeViewModel.setSelectedRecipe(recipeId);
        mRecipeViewModel.setSelectedRecipeStep(stepId);

        setTitleText();
    }

    private void setTitleText() {
        mRecipeViewModel.getSelectedRecipe().observe(this, recipe -> {
            if (recipe != null) {
                setTitle(recipe.getName() + " Recipe");
            }
        });
    }

    private void initIngredientViewModel(int recipeId) {
        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        mRecipeViewModel.setSelectedRecipeStep(recipeId);
    }
}
