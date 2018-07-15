package com.nverno.bakingtime.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.model.Recipe;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

public class RecipeStepDetailActivity extends AppCompatActivity {

    private static final String RECIPE_ID = "RECIPE_ID";
    private static final String STEP_ID = "STEP_ID";

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
        }
    }

    // Initialize the view model to set the recipe and the current step.
    private void initViewModel(int recipeId, int stepId) {
        RecipeViewModel recipeViewModel = ViewModelProviders.of(this)
                .get(RecipeViewModel.class);

        recipeViewModel.setSelectedRecipe(recipeId);
        recipeViewModel.setSelectedRecipeStep(stepId);

        recipeViewModel.getSelectedRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if (recipe != null) {
                    setTitle(recipe.getName() + " Recipe");
                }
            }
        });
    }
}
